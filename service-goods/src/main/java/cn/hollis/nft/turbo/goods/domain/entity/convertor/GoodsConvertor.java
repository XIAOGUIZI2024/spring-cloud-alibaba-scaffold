package cn.hollis.nft.turbo.goods.domain.entity.convertor;

import cn.hollis.nft.turbo.api.goods.model.GoodsVO;
import cn.hollis.nft.turbo.goods.domain.entity.Goods;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 商品实体 ↔ VO 转换器
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface GoodsConvertor {

    GoodsConvertor INSTANCE = Mappers.getMapper(GoodsConvertor.class);

    @Mapping(target = "createTime", source = "gmtCreate")
    GoodsVO mapToVo(Goods goods);

    @Mapping(target = "createTime", source = "gmtCreate")
    List<GoodsVO> mapToVo(List<Goods> goodsList);
}
