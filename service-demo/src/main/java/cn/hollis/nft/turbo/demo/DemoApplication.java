package cn.hollis.nft.turbo.demo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Demo 服务启动类
 */
@SpringBootApplication(scanBasePackages = {"cn.hollis.nft.turbo.demo"})
@EnableDubbo
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
