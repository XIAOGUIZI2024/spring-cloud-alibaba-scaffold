package cn.hollis.nft.turbo.web.configuration;

import cn.hollis.nft.turbo.web.handler.GlobalWebExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 层自动配置（最小脚手架版本）
 * <p>
 * 仅包含全局异常处理器。如需认证/鉴权，请引入 common-sa-token 模块。
 *
 * @author Hollis
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    GlobalWebExceptionHandler globalWebExceptionHandler() {
        return new GlobalWebExceptionHandler();
    }

}
