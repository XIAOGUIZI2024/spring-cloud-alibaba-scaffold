package cn.hollis.nft.turbo.api.goods.request;

import cn.hollis.nft.turbo.api.goods.constant.GoodsType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建商品请求
 */
@Data
public class GoodsCreateRequest {

    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    @NotNull(message = "商品类型不能为空")
    private GoodsType goodsType;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    private Integer inventory;

    private String coverUrl;
}
