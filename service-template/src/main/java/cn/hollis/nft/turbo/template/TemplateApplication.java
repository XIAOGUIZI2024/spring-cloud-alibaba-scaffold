package cn.hollis.nft.turbo.template;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 微服务模板启动类
 * <p>
 * 复制此模块创建新服务时：
 * 1. 修改 pom.xml 中的 application.name
 * 2. 修改包名 cn.hollis.nft.turbo.xxx 为你的服务包名
 * 3. 修改 scanBasePackages 为你的服务包名
 * 4. 按 DDD 分层创建 facade / domain / infrastructure 包
 *
 * @author Hollis
 */
@SpringBootApplication(scanBasePackages = {"cn.hollis.nft.turbo.template"})
@EnableDubbo
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

}
