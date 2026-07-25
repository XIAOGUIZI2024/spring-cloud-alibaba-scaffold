package cn.hollis.nft.turbo.goods.infrastructure.mapper;

import cn.hollis.nft.turbo.goods.domain.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品 Mapper
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 根据商品编号查询
     */
    Goods findByGoodsId(@NotNull String goodsId);

    /**
     * 根据商品编号更新
     */
    int updateByGoodsId(@Param("goods") Goods goods);

    /**
     * 根据商品编号删除（软删除）
     */
    int deleteByGoodsId(@Param("goodsId") String goodsId);
}
