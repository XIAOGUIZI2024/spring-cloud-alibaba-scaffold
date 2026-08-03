package cn.hollis.nft.turbo.auth.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hollis.nft.turbo.api.user.request.UserQueryRequest;
import cn.hollis.nft.turbo.api.user.request.UserRegisterRequest;
import cn.hollis.nft.turbo.api.user.response.UserOperatorResponse;
import cn.hollis.nft.turbo.api.user.response.UserQueryResponse;
import cn.hollis.nft.turbo.api.user.response.data.UserInfo;
import cn.hollis.nft.turbo.api.user.service.UserFacadeService;
import cn.hollis.nft.turbo.web.vo.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器 - 登录/注册/登出
 * <p>
 * 通过 Dubbo RPC 调用 service-user 查询和注册用户
 *
 * @author DaDagui
 */
@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    @DubboReference(version = "1.0.0")
    private NoticeFacadeService noticeFacadeService;


    /**
     * 默认超过登陆时间：7天
     */
    private static final Integer DEFAULT_LOGIN_SESSION_TIMEOUT = 60 * 60 * 24 * 7;


    /**
     * 验证码发送请求
     * @param telephone
     * @return
     */
    @GetMapping("/sendCaptcha")
    public Result<Boolean> sendCaptcha(@IsMobile String telephone) {
        NoticeResponse noticeResponse = noticeFacadeService.generateAndSendSmsCaptcha(telephone);
        return Result.success(noticeResponse.getSuccess());
    }

    /**
     * 登录（手机号登录，用户不存在则自动注册）
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginParam loginParam) {
        // 查询用户是否存在
        UserQueryResponse<UserInfo> queryResponse = userFacadeService.queryByTelephone(loginParam.getTelephone());
        UserInfo userInfo = queryResponse.getData();

        if (userInfo == null) {
            // 不存在 → 远程调用 UserService 注册
            UserRegisterRequest registerRequest = new UserRegisterRequest();
            registerRequest.setTelephone(loginParam.getTelephone());
            registerRequest.setInviteCode(loginParam.getInviteCode());
            UserOperatorResponse registerResult = userFacadeService.register(registerRequest);

            if (!registerResult.getSuccess()) {
                return Result.error(registerResult.getResponseCode(), registerResult.getResponseMessage());
            }
            // 重新查询
            queryResponse = userFacadeService.queryByTelephone(loginParam.getTelephone());
            userInfo = queryResponse.getData();
        }

        // Sa-Token 登录
        StpUtil.login(userInfo.getUserId(),
                new SaLoginModel()
                        .setIsLastingCookie(loginParam.getRememberMe())
                        .setTimeout(DEFAULT_LOGIN_SESSION_TIMEOUT));
        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);

        String token = StpUtil.getTokenValue();
        log.info("用户登录成功, userId={}", userInfo.getUserId());
        return Result.success(token);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        StpUtil.logout();
        return Result.success(true);
    }
}
