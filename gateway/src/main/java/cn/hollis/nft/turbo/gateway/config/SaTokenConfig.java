package cn.hollis.nft.turbo.gateway.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Gateway 鉴权配置
 * <p>
 * 三层鉴权：登录校验 → 角色校验 → 权限校验<br>
 * 按需修改下面的规则，不需要改代码结构。
 */
@Slf4j
@Configuration
public class SaTokenConfig {

    @PostConstruct
    public void init() {
        log.info("=== Gateway 鉴权已加载 ===");
    }

    @Bean
    @Order(-100)
    public SaReactorFilter saReactorFilter() {
        return new SaReactorFilter()
                // ---- 拦截规则 ----
                .addInclude("/**")
                .addExclude(
                        "/auth/**",      // 登录/注册 - 放行
                        "/favicon.ico"   // 浏览器图标 - 放行
                )

                // ---- 鉴权规则 ----
                .setAuth(obj -> {
                    // ===== 第一层：登录校验（所有接口都要过） =====
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());

                    // ===== 第二层：角色校验（按路径匹配） =====
                    // 示例：/admin/** 需要 ADMIN 角色
                    // SaRouter.match("/admin/**")
                    //         .check(r -> StpUtil.checkRole("ADMIN"));

                    // ===== 第三层：权限校验（按路径匹配） =====
                    // 示例：敏感操作需要特定权限
                    // SaRouter.match("/goods/create", "/goods/update", "/goods/delete")
                    //         .check(r -> StpUtil.checkPermission("goods:write"));

                    // 示例：只读接口
                    // SaRouter.match("/goods/query", "/goods/pageQuery")
                    //         .check(r -> StpUtil.checkPermissionOr("goods:read", "goods:write"));

                })

                // ---- 异常处理 ----
                .setError(e -> {
                    if (e instanceof NotLoginException) {
                        return SaResult.error("请先登录");
                    }
                    if (e instanceof NotRoleException) {
                        return SaResult.error("无此角色权限");
                    }
                    if (e instanceof NotPermissionException) {
                        return SaResult.error("无此操作权限");
                    }
                    return SaResult.error(e.getMessage());
                });
    }
}
