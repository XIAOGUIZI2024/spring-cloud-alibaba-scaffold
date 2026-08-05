package cn.hollis.nft.turbo.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hollis.nft.turbo.api.chain.request.ChainProcessRequest;
import cn.hollis.nft.turbo.api.chain.response.ChainProcessResponse;
import cn.hollis.nft.turbo.api.chain.response.data.ChainCreateData;
import cn.hollis.nft.turbo.api.chain.service.ChainFacadeService;
import cn.hollis.nft.turbo.api.user.request.UserActiveRequest;
import cn.hollis.nft.turbo.api.user.request.UserAuthRequest;
import cn.hollis.nft.turbo.api.user.request.UserModifyRequest;
import cn.hollis.nft.turbo.api.user.response.UserOperatorResponse;
import cn.hollis.nft.turbo.api.user.response.data.BasicUserInfo;
import cn.hollis.nft.turbo.api.user.response.data.UserInfo;
import cn.hollis.nft.turbo.file.FileService;
import cn.hollis.nft.turbo.user.domain.entity.User;
import cn.hollis.nft.turbo.user.domain.entity.convertor.UserConvertor;
import cn.hollis.nft.turbo.user.domain.service.UserService;
import cn.hollis.nft.turbo.user.infrastructure.exception.UserException;
import cn.hollis.nft.turbo.user.param.UserAuthParam;
import cn.hollis.nft.turbo.user.param.UserModifyParam;
import cn.hollis.nft.turbo.web.vo.Result;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static cn.hollis.nft.turbo.api.common.constant.CommonConstant.APP_NAME_UPPER;
import static cn.hollis.nft.turbo.api.common.constant.CommonConstant.SEPARATOR;
import static cn.hollis.nft.turbo.user.infrastructure.exception.UserErrorCode.USER_CREATE_CHAIN_FAIL;
import static cn.hollis.nft.turbo.user.infrastructure.exception.UserErrorCode.USER_NOT_EXIST;
import static cn.hollis.nft.turbo.user.infrastructure.exception.UserErrorCode.USER_PASSWD_CHECK_FAIL;
import static cn.hollis.nft.turbo.user.infrastructure.exception.UserErrorCode.USER_UPLOAD_PICTURE_FAIL;

/**
 * 用户信息控制器
 *
 * @author DaDagui
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @DubboReference(version = "1.0.0")
    private ChainFacadeService chainFacadeService;

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/getUserInfo")
    public Result<UserInfo> getUserInfo() {
        String userId = (String) StpUtil.getLoginId();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            throw new UserException(USER_NOT_EXIST);
        }
        return Result.success(UserConvertor.INSTANCE.mapToVo(user));
    }

    /**
     * 通过手机号查询用户（简化信息）
     */
    @GetMapping("/queryUserByTel")
    public Result<BasicUserInfo> queryUserByTel(String telephone) {
        User user = userService.findByTelephone(telephone);
        if (user == null) {
            throw new UserException(USER_NOT_EXIST);
        }
        return Result.success(UserConvertor.INSTANCE.mapToBasicVo(user));
    }

    /**
     * 修改昵称
     */
    @PostMapping("/modifyNickName")
    public Result<Boolean> modifyNickName(@Valid @RequestBody UserModifyParam userModifyParam) {
        String userId = (String) StpUtil.getLoginId();

        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setNickName(userModifyParam.getNickName());

        Boolean success = userService.modify(userModifyRequest).getSuccess();
        return Result.success(success);
    }

    /**
     * 修改密码
     */
    @PostMapping("/modifyPassword")
    public Result<Boolean> modifyPassword(@Valid @RequestBody UserModifyParam userModifyParam) {
        String userId = (String) StpUtil.getLoginId();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            throw new UserException(USER_NOT_EXIST);
        }
        if (!StrUtil.equals(user.getPasswordHash(), DigestUtil.md5Hex(userModifyParam.getOldPassword()))) {
            throw new UserException(USER_PASSWD_CHECK_FAIL);
        }

        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setPassword(userModifyParam.getNewPassword());

        Boolean success = userService.modify(userModifyRequest).getSuccess();
        return Result.success(success);
    }

    /**
     * 修改头像（上传到 OSS / Mock，然后更新用户头像地址）
     */
    @PostMapping("/modifyProfilePhoto")
    public Result<String> modifyProfilePhoto(@RequestParam("file_data") MultipartFile file) throws Exception {
        String userId = (String) StpUtil.getLoginId();
        // TODO 根据自己配置的 OSS Bucket 域名修改前缀
        String prefix = "https://nfturbo-file.oss-cn-hangzhou.aliyuncs.com/";

        if (null == file) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        String filename = file.getOriginalFilename();
        InputStream fileStream = file.getInputStream();
        String path = "profile/" + userId + "/" + filename;
        boolean res = fileService.upload(path, fileStream);
        if (!res) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        //修改信息
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setProfilePhotoUrl(prefix + path);
        Boolean success = userService.modify(userModifyRequest).getSuccess();
        if (!success) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        return Result.success(prefix + path);
    }

    /**
     * 实名认证 + 上链激活
     * <p>
     * 实名认证成功后调用 service-chain 创建链地址，用链地址激活账户（state → ACTIVE）
     */
    @PostMapping("/auth")
    public Result<Boolean> auth(@Valid @RequestBody UserAuthParam userAuthParam) {
        String userId = (String) StpUtil.getLoginId();

        UserAuthRequest userAuthRequest = new UserAuthRequest();
        userAuthRequest.setUserId(Long.valueOf(userId));
        userAuthRequest.setRealName(userAuthParam.getRealName());
        userAuthRequest.setIdCard(userAuthParam.getIdCard());

        UserOperatorResponse authResult = userService.auth(userAuthRequest);
        if (authResult.getSuccess()) {
            //实名认证成功，需要上链创建链地址
            ChainProcessRequest chainCreateRequest = new ChainProcessRequest();
            chainCreateRequest.setUserId(userId);
            String identifier = APP_NAME_UPPER + SEPARATOR + authResult.getUser().getUserRole() + SEPARATOR
                    + authResult.getUser().getUserId();
            chainCreateRequest.setIdentifier(identifier);
            ChainProcessResponse<ChainCreateData> chainProcessResponse = chainFacadeService.createAddr(chainCreateRequest);
            if (chainProcessResponse.getSuccess()) {
                //用链地址激活账户
                ChainCreateData chainCreateData = chainProcessResponse.getData();
                UserActiveRequest userActiveRequest = new UserActiveRequest();
                userActiveRequest.setUserId(Long.valueOf(userId));
                userActiveRequest.setBlockChainUrl(chainCreateData.getAccount());
                userActiveRequest.setBlockChainPlatform(chainCreateData.getPlatform());
                UserOperatorResponse activeResponse = userService.active(userActiveRequest);
                if (activeResponse.getSuccess()) {
                    refreshUserInSession(userId);
                    return Result.success(true);
                }
                return Result.error(activeResponse.getResponseCode(), activeResponse.getResponseMessage());
            } else {
                throw new UserException(USER_CREATE_CHAIN_FAIL);
            }
        }
        return Result.error(authResult.getResponseCode(), authResult.getResponseMessage());
    }

    private void refreshUserInSession(String userId) {
        User user = userService.findById(Long.valueOf(userId));
        UserInfo userInfo = UserConvertor.INSTANCE.mapToVo(user);
        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
    }
}
