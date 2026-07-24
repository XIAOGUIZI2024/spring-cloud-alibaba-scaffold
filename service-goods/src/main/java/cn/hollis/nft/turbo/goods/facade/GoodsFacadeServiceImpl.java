package cn.hollis.nft.turbo.goods.facade;

import cn.hollis.nft.turbo.api.goods.request.GoodsCreateRequest;
import cn.hollis.nft.turbo.api.goods.request.GoodsPageQueryRequest;
import cn.hollis.nft.turbo.api.goods.request.GoodsQueryRequest;
import cn.hollis.nft.turbo.api.goods.response.GoodsQueryResponse;
import cn.hollis.nft.turbo.api.goods.service.GoodsFacadeService;
import cn.hollis.nft.turbo.base.response.PageResponse;
import cn.hollis.nft.turbo.rpc.facade.Facade;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 商品门面服务实现
 */
@DubboService(version = "1.0.0")
public class GoodsFacadeServiceImpl implements GoodsFacadeService {

    @Override
    @Facade
    public GoodsQueryResponse create(GoodsCreateRequest request) {
        // TODO: 实现商品创建
        throw new UnsupportedOperationException("TODO: 实现商品创建");
    }

    @Override
    public GoodsQueryResponse query(GoodsQueryRequest request) {
        // TODO: 实现商品查询
        throw new UnsupportedOperationException("TODO: 实现商品查询");
    }

    @Override
    public PageResponse<GoodsQueryResponse> pageQuery(GoodsPageQueryRequest request) {
        // TODO: 实现分页查询
        throw new UnsupportedOperationException("TODO: 实现分页查询");
    }
}
