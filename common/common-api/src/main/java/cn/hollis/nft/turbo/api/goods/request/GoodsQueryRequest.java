package cn.hollis.nft.turbo.api.goods.request;

import cn.hollis.nft.turbo.api.goods.constant.GoodsType;
import lombok.Data;

/**
 * 商品查询请求
 */
@Data
public class GoodsQueryRequest {

    private String goodsId;     // 商品编号
    private GoodsType goodsType; // 商品类型
}
