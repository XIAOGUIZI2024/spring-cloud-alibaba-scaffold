package cn.hollis.nft.turbo.goods.domain.service;

import cn.hollis.nft.turbo.api.goods.constant.GoodsState;
import cn.hollis.nft.turbo.goods.domain.entity.Goods;
import cn.hollis.nft.turbo.goods.infrastructure.mapper.GoodsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 商品领域服务
 */
@Service
public class GoodsService extends ServiceImpl<GoodsMapper, Goods> {

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 通过商品编号查询
     */
    public Goods findByGoodsId(String goodsId) {
        return goodsMapper.findByGoodsId(goodsId);
    }

    /**
     * 创建商品
     */
    public Goods create(Goods goods) {
        goods.setGoodsId(UUID.randomUUID().toString());
        goods.setState(GoodsState.WAIT_FOR_SALE);
        save(goods);
        return goods;
    }

    /**
     * 更新商品
     */
    public Goods update(Goods goods) {
        Goods existing = findByGoodsId(goods.getGoodsId());
        if (existing == null) {
            return null;
        }
        goods.setId(existing.getId());
        updateById(goods);
        return goods;
    }

    /**
     * 删除商品（软删除）
     */
    public boolean deleteByGoodsId(String goodsId) {
        return goodsMapper.deleteByGoodsId(goodsId) > 0;
    }

    /**
     * 分页查询
     */
    public Page<Goods> pageQuery(Page<Goods> page, String keyword, String goodsType,
                                  String color, String origin, String shape,
                                  String transparency, String variety) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(keyword != null, Goods::getGoodsName, keyword)
                .eq(goodsType != null, Goods::getGoodsType, goodsType)
                .eq(color != null, Goods::getColor, color)
                .eq(origin != null, Goods::getOrigin, origin)
                .eq(shape != null, Goods::getShape, shape)
                .eq(transparency != null, Goods::getTransparency, transparency)
                .eq(variety != null, Goods::getVariety, variety)
                .orderByDesc(Goods::getGmtCreate);
        return page(page, wrapper);
    }
}
