package cn.hollis.nft.turbo.goods;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 商品服务启动类
 */
@SpringBootApplication(scanBasePackages = {"cn.hollis.nft.turbo.goods"})
@EnableDubbo
@MapperScan("cn.hollis.nft.turbo.goods.infrastructure.mapper")
public class GoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

}
