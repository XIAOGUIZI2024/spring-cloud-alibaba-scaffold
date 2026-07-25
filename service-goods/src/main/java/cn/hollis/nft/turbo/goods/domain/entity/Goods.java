package cn.hollis.nft.turbo.goods.domain.entity;

import cn.hollis.nft.turbo.api.goods.constant.GoodsState;
import cn.hollis.nft.turbo.api.goods.constant.GoodsType;
import cn.hollis.nft.turbo.datasource.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品实体
 */
@Setter
@Getter
@TableName("goods")
public class Goods extends BaseEntity {

    private String goodsId;      // 商品编号
    private String goodsName;    // 商品名称
    private GoodsType goodsType; // 商品类型
    private GoodsState state;    // 状态
    private BigDecimal price;    // 价格
    private Integer inventory;   // 库存
    private Integer lockedInventory; // 锁定库存
    private String coverUrl;     // 封面图
    private Date saleTime;       // 开售时间
    private String color; //颜色
    private String origin; //产地
    private String shape; // 形状：圆形、椭圆、方形、随形等
    private String transparency; //透明度/水头：透明、半透明、不透明
    private String variety;// 种水：玻璃种、高冰种、冰种、糯冰种、糯化种、细糯种、糯种、豆种等
}
