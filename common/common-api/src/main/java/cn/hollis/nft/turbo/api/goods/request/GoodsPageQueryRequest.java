package cn.hollis.nft.turbo.api.goods.request;

import cn.hollis.nft.turbo.base.request.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分页查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsPageQueryRequest extends PageRequest {

    private String keyword;     // 搜索关键词
    private String state;       // 商品状态
    private String goodsType;   // 商品类型
}
