package cn.hollis.nft.turbo.user.controller;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器 - 登录/注册/登出
 *
 * @author Hollis
 */
@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    private static final Integer DEFAULT_LOGIN_SESSION_TIMEOUT = 60 * 60 * 24 * 7;

    /**
     * 登录（手机号+验证码，或自动注册）
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginParam loginParam) {
        // 查询用户是否存在
        UserQueryRequest queryRequest = new UserQueryRequest(loginParam.getTelephone());
        UserQueryResponse<UserInfo> queryResponse = userFacadeService.query(queryRequest);
        UserInfo userInfo = queryResponse.getData();

        if (userInfo == null) {
            // 不存在 → 自动注册
            UserRegisterRequest registerRequest = new UserRegisterRequest();
            registerRequest.setTelephone(loginParam.getTelephone());
            registerRequest.setInviteCode(loginParam.getInviteCode());
            UserOperatorResponse registerResult = userFacadeService.register(registerRequest);

            if (!registerResult.getSuccess()) {
                return Result.error(registerResult.getResponseCode(), registerResult.getResponseMessage());
            }
            // 重新查询
            queryResponse = userFacadeService.query(queryRequest);
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
