package cn.hollis.nft.turbo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API 网关启动类（最小版本）
 * <p>
 * 依赖：Spring Cloud Gateway + Nacos 服务发现<br>
 * 不含：认证鉴权(Sa-Token)、限流(Sentinel)<br>
 * 需要时请自行引入对应模块
 *
 * @author Hollis
 */
@SpringBootApplication(scanBasePackages = "cn.hollis.nft.turbo.gateway")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
