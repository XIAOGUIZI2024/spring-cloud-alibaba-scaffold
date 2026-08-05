package cn.hollis.nft.turbo.admin;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 管理后台启动类
 * <p>
 * 独立部署（端口 9001），不经过网关，通过 @DubboReference 调用用户领域服务
 *
 * @author DaDagui
 */
@SpringBootApplication(scanBasePackages = {"cn.hollis.nft.turbo.admin"})
@EnableDubbo
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
