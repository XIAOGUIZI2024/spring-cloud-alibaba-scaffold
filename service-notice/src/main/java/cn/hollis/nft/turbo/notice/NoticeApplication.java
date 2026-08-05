package cn.hollis.nft.turbo.notice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 通知服务启动类
 *
 * @author Hollis
 */
@SpringBootApplication(scanBasePackages = {"cn.hollis.nft.turbo.notice"})
@EnableDubbo
@MapperScan("cn.hollis.nft.turbo.notice.infrastructure.mapper")
public class NoticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoticeApplication.class, args);
    }

}
