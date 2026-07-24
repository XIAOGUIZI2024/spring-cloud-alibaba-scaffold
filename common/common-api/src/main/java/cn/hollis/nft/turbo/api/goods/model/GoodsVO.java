package cn.hollis.nft.turbo.api.goods.model;

import cn.hollis.nft.turbo.api.goods.constant.GoodsState;
import cn.hollis.nft.turbo.api.goods.constant.GoodsType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品 VO
 */
@Data
public class GoodsVO implements Serializable {

    private Long id;
    private String goodsId;         // 商品编号
    private String goodsName;       // 商品名称
    private GoodsType goodsType;    // 商品类型
    private GoodsState state;       // 状态
    private BigDecimal price;       // 价格
    private Integer inventory;      // 库存
    private String coverUrl;        // 封面图
    private Date saleTime;          // 开售时间
    private Date createTime;        // 创建时间
}
