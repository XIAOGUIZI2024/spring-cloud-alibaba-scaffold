package cn.hollis.nft.turbo.chain;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 链服务启动类
 * <p>
 * 独立部署（端口 8090），通过 Dubbo 对外提供 ChainFacadeService，
 * 供 service-user 实名认证后创建链地址激活账户等场景调用
 *
 * @author DaDagui
 */
@SpringBootApplication(scanBasePackages = "cn.hollis.nft.turbo.chain")
@EnableDubbo
@EnableScheduling
public class ChainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChainApplication.class, args);
    }

}
