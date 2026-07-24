package cn.hollis.nft.turbo.demo.controller;

import cn.hollis.nft.turbo.api.demo.request.HelloRequest;
import cn.hollis.nft.turbo.api.demo.response.HelloResponse;
import cn.hollis.nft.turbo.base.response.SingleResponse;
import cn.hollis.nft.turbo.web.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demo REST 控制器（直接 HTTP 访问，不走 Dubbo）
 */
@RestController
public class DemoController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public String health() {
        return "Demo Service is running! ✅";
    }

    /**
     * HTTP 直接调用（不经过 Dubbo）
     */
    @GetMapping("/hello")
    public Result<HelloResponse> hello(@RequestParam(defaultValue = "World") String name) {
        HelloResponse response = new HelloResponse();
        response.setGreeting("Hello, " + name + "! 这是来自 Demo Service 的 HTTP 响应 🎉");
        response.setTimestamp(System.currentTimeMillis());
        return Result.success(response);
    }
}
