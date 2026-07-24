package cn.hollis.nft.turbo.demo.facade;

import cn.hollis.nft.turbo.api.demo.request.HelloRequest;
import cn.hollis.nft.turbo.api.demo.response.HelloResponse;
import cn.hollis.nft.turbo.api.demo.service.HelloFacadeService;
import cn.hollis.nft.turbo.base.response.SingleResponse;
import cn.hollis.nft.turbo.rpc.facade.Facade;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * Hello 门面服务实现（Dubbo RPC 暴露）
 */
@Slf4j
@DubboService(version = "1.0.0")
public class HelloFacadeServiceImpl implements HelloFacadeService {

    @Override
    @Facade
    public SingleResponse<HelloResponse> hello(HelloRequest request) {
        log.info("收到 hello 请求, name={}", request.getName());

        HelloResponse response = new HelloResponse();
        response.setGreeting("Hello, " + request.getName() + "! 欢迎使用微服务脚手架 🚀");
        response.setTimestamp(System.currentTimeMillis());

        return SingleResponse.of(response);
    }
}
