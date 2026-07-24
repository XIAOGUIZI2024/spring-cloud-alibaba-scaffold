package cn.hollis.nft.turbo.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 认证服务启动类
 * <p>
 * 职责：登录/注册/登出，管理 Sa-Token Session<br>
 * 不连数据库，通过 Dubbo RPC 调用 service-user
 */
@SpringBootApplication(scanBasePackages = {"cn.hollis.nft.turbo.auth"})
@EnableDubbo
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
