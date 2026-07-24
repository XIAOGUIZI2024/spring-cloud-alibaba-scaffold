package cn.hollis.nft.turbo.user.domain.service.config;

import cn.hollis.nft.turbo.user.domain.service.AuthService;
import cn.hollis.nft.turbo.user.domain.service.MockAuthServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 认证配置（简化版 - 使用 Mock 实现）
 */
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuthService authService() {
        return new MockAuthServiceImpl();
    }

}
