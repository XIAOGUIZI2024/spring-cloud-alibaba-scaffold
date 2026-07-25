package cn.hollis.nft.turbo.gateway.config;

import cn.dev33.satoken.exception.NotLoginException;
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
 * Sa-Token 最小鉴权配置
 * <p>
 * 只做一件事：校验登录态。<br>
 * 所有请求需要带有效 token，/auth/** 放行。
 */
@Slf4j
@Configuration
public class SaTokenConfig {

    @PostConstruct
    public void init() {
        log.info("=== SaTokenConfig 已加载 ===");
    }

    @Bean
    @Order(-100)
    public SaReactorFilter saReactorFilter() {
        log.info("=== 注册 SaReactorFilter ===");
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude("/auth/**", "/favicon.ico")
                .setAuth(obj -> {
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());
                })
                .setError(e -> {
                    if (e instanceof NotLoginException) {
                        return SaResult.error("请先登录");
                    }
                    return SaResult.error(e.getMessage());
                });
    }
}
