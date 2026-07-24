package cn.hollis.nft.turbo.api.goods.response;

import cn.hollis.nft.turbo.api.goods.model.GoodsVO;
import cn.hollis.nft.turbo.base.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品查询响应
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsQueryResponse extends BaseResponse {

    private GoodsVO goods;
}
