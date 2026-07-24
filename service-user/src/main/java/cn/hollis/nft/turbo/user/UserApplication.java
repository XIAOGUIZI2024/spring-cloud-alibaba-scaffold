package cn.hollis.nft.turbo.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户服务启动类
 * <p>
 * 包含：登录/注册/用户管理
 */
@SpringBootApplication(scanBasePackages = {"cn.hollis.nft.turbo.user"})
@EnableDubbo
@MapperScan("cn.hollis.nft.turbo.user.infrastructure.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
