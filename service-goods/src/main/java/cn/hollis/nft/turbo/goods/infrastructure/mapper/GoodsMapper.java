package cn.hollis.nft.turbo.goods.infrastructure.mapper;

import cn.hollis.nft.turbo.goods.domain.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品 Mapper
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 根据商品编号查询
     */
    Goods findByGoodsId(@NotNull String goodsId);
}
