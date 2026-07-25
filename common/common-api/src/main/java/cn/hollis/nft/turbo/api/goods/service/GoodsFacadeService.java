package cn.hollis.nft.turbo.api.goods.service;

import cn.hollis.nft.turbo.api.goods.request.GoodsCreateRequest;
import cn.hollis.nft.turbo.api.goods.request.GoodsPageQueryRequest;
import cn.hollis.nft.turbo.api.goods.request.GoodsQueryRequest;
import cn.hollis.nft.turbo.api.goods.request.GoodsUpdateRequest;
import cn.hollis.nft.turbo.api.goods.response.GoodsQueryResponse;
import cn.hollis.nft.turbo.base.response.PageResponse;

/**
 * 商品服务接口
 */
public interface GoodsFacadeService {

    /**
     * 创建商品
     */
    GoodsQueryResponse create(GoodsCreateRequest request);

    /**
     * 查询商品
     */
    GoodsQueryResponse query(GoodsQueryRequest request);

    /**
     * 分页查询商品
     */
    PageResponse<GoodsQueryResponse> pageQuery(GoodsPageQueryRequest request);

    /**
     * 更新商品
     */
    GoodsQueryResponse update(GoodsUpdateRequest request);

    /**
     * 删除商品
     */
    GoodsQueryResponse delete(GoodsQueryRequest request);
}
