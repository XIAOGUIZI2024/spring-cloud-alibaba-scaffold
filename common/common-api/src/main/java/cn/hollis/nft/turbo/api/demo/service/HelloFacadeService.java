package cn.hollis.nft.turbo.api.demo.service;

import cn.hollis.nft.turbo.api.demo.request.HelloRequest;
import cn.hollis.nft.turbo.api.demo.response.HelloResponse;
import cn.hollis.nft.turbo.base.response.SingleResponse;

/**
 * Demo 门面服务接口
 */
public interface HelloFacadeService {

    SingleResponse<HelloResponse> hello(HelloRequest request);
}
