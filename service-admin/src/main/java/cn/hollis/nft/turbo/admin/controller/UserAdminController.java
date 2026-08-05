package cn.hollis.nft.turbo.admin.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hollis.nft.turbo.admin.infrastructure.exception.AdminException;
import cn.hollis.nft.turbo.admin.param.AdminLoginParam;
import cn.hollis.nft.turbo.admin.vo.AdminLoginVO;
import cn.hollis.nft.turbo.api.user.constant.UserRole;
import cn.hollis.nft.turbo.api.user.request.UserPageQueryRequest;
import cn.hollis.nft.turbo.api.user.request.UserQueryRequest;
import cn.hollis.nft.turbo.api.user.response.UserOperatorResponse;
import cn.hollis.nft.turbo.api.user.response.UserQueryResponse;
import cn.hollis.nft.turbo.api.user.response.data.UserInfo;
import cn.hollis.nft.turbo.api.user.service.UserFacadeService;
import cn.hollis.nft.turbo.api.user.service.UserManageFacadeService;
import cn.hollis.nft.turbo.base.response.PageResponse;
import cn.hollis.nft.turbo.web.util.MultiResultConvertor;
import cn.hollis.nft.turbo.web.vo.MultiResult;
import cn.hollis.nft.turbo.web.vo.Result;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import static cn.hollis.nft.turbo.admin.infrastructure.exception.AdminErrorCode.ADMIN_USER_NOT_EXIST;

/**
 * 用户后台管理
 * <p>
 * 独立部署（端口 9001），不经过网关，通过 Dubbo RPC 调用 service-user
 *
 * @author DaDagui
 */
@Slf4j
@RestController
@RequestMapping("admin/user")
@CrossOrigin(origins = "*")
public class UserAdminController {

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    @DubboReference(version = "1.0.0")
    private UserManageFacadeService userManageFacadeService;

    /**
     * 默认登录超时时间：7天
     */
    private static final Integer DEFAULT_LOGIN_SESSION_TIMEOUT = 60 * 60 * 24 * 7;

    /**
     * 获取当前管理员信息
     */
    @GetMapping("/getUserInfo")
    public Result<UserInfo> getUserInfo() {
        String userId = (String) StpUtil.getLoginId();
        UserQueryRequest request = new UserQueryRequest(Long.valueOf(userId));
        UserQueryResponse<UserInfo> userQueryResponse = userFacadeService.query(request);
        UserInfo userInfo = userQueryResponse.getData();
        if (userInfo == null) {
            throw new AdminException(ADMIN_USER_NOT_EXIST);
        }
        return Result.success(userInfo);
    }

    /**
     * 用户列表分页查询
     */
    @GetMapping("/userList")
    public MultiResult<UserInfo> userList(@NotBlank String state, String keyWord, int pageSize, int currentPage) {
        UserPageQueryRequest userPageQueryRequest = new UserPageQueryRequest();
        userPageQueryRequest.setState(state);
        userPageQueryRequest.setKeyWord(keyWord);
        userPageQueryRequest.setCurrentPage(currentPage);
        userPageQueryRequest.setPageSize(pageSize);
        PageResponse<UserInfo> pageResponse = userFacadeService.pageQuery(userPageQueryRequest);
        return MultiResultConvertor.convert(pageResponse);
    }

    /**
     * 管理员注册（不提供，通过数据订正初始化管理员账号）
     */
    @PostMapping("/registerAdmin")
    public Result<Boolean> registerAdmin(@Valid String phone) {
        // 参考项目约定：不直接提供管理员注册功能，通过 SQL 数据订正初始化管理员账号
        // INSERT INTO `users` (...) VALUES (...,'ADMIN',0,0);
        return null;
    }

    /**
     * 管理员登录（手机号 + 密码）
     */
    @PostMapping("/login")
    public Result<AdminLoginVO> login(@Valid @RequestBody AdminLoginParam loginParam) {
        // 手机号+密码查询用户，校验是否为管理员
        UserQueryRequest userQueryRequest = new UserQueryRequest(loginParam.getTelephone(), loginParam.getPassword());
        UserQueryResponse<UserInfo> userQueryResponse = userFacadeService.query(userQueryRequest);
        UserInfo userInfo = userQueryResponse.getData();

        // 用户不存在（或密码错误）或者不是管理员用户，不能登录
        if (userInfo == null || !UserRole.ADMIN.equals(userInfo.getUserRole())) {
            return Result.error(ADMIN_USER_NOT_EXIST.getCode(), ADMIN_USER_NOT_EXIST.getMessage());
        }

        StpUtil.login(userInfo.getUserId(), new SaLoginModel()
                .setIsLastingCookie(loginParam.getRememberMe())
                .setTimeout(DEFAULT_LOGIN_SESSION_TIMEOUT));
        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);

        return Result.success(new AdminLoginVO(userInfo));
    }

    /**
     * 管理员登出
     */
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        StpUtil.logout();
        return Result.success(true);
    }

    /**
     * 冻结用户
     */
    @PostMapping("/freeze")
    public Result<UserOperatorResponse> freeze(Long userId) {
        checkAdmin();
        UserOperatorResponse res = userManageFacadeService.freeze(userId);
        refreshUserInSession(userId);
        return Result.success(res);
    }

    /**
     * 解冻用户
     */
    @PostMapping("/unfreeze")
    public Result<UserOperatorResponse> unfreeze(Long userId) {
        checkAdmin();
        UserOperatorResponse res = userManageFacadeService.unfreeze(userId);
        refreshUserInSession(userId);
        return Result.success(res);
    }

    /**
     * 校验当前操作者是否为管理员
     */
    private void checkAdmin() {
        String adminUserId = (String) StpUtil.getLoginId();
        UserQueryRequest adminQueryRequest = new UserQueryRequest(Long.valueOf(adminUserId));
        UserQueryResponse<UserInfo> userQueryResponse = userFacadeService.query(adminQueryRequest);
        UserInfo userInfo = userQueryResponse.getData();
        if (userInfo == null || !UserRole.ADMIN.equals(userInfo.getUserRole())) {
            throw new AdminException(ADMIN_USER_NOT_EXIST);
        }
    }

    /**
     * 重新查询用户信息，更新登录的 session，确保权限实时更新
     */
    private void refreshUserInSession(Long userId) {
        UserQueryRequest userQueryRequest = new UserQueryRequest(userId);
        UserQueryResponse userQueryResponse = userFacadeService.query(userQueryRequest);
        StpUtil.getSessionByLoginId(userId).set(userId.toString(), userQueryResponse.getData());
    }
}
