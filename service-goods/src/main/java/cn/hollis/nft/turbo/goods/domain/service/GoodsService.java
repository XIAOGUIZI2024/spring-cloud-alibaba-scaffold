package cn.hollis.nft.turbo.goods.domain.service;

import cn.hollis.nft.turbo.goods.domain.entity.Goods;
import cn.hollis.nft.turbo.goods.infrastructure.mapper.GoodsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // TODO: 添加业务方法（创建商品、修改库存、上下架等）
}
