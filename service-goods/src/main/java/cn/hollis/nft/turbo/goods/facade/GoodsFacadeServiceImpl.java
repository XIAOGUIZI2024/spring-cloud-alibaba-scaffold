package cn.hollis.nft.turbo.goods.facade;

import cn.hollis.nft.turbo.api.goods.model.GoodsVO;
import cn.hollis.nft.turbo.api.goods.request.GoodsCreateRequest;
import cn.hollis.nft.turbo.api.goods.request.GoodsPageQueryRequest;
import cn.hollis.nft.turbo.api.goods.request.GoodsQueryRequest;
import cn.hollis.nft.turbo.api.goods.request.GoodsUpdateRequest;
import cn.hollis.nft.turbo.api.goods.response.GoodsQueryResponse;
import cn.hollis.nft.turbo.api.goods.service.GoodsFacadeService;
import cn.hollis.nft.turbo.base.response.PageResponse;
import cn.hollis.nft.turbo.goods.domain.entity.Goods;
import cn.hollis.nft.turbo.goods.domain.entity.convertor.GoodsConvertor;
import cn.hollis.nft.turbo.goods.domain.service.GoodsService;
import cn.hollis.nft.turbo.rpc.facade.Facade;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 商品门面服务实现
 */
@DubboService(version = "1.0.0")
public class GoodsFacadeServiceImpl implements GoodsFacadeService {

    @Autowired
    private GoodsService goodsService;

    @Override
    @Facade
    public GoodsQueryResponse create(GoodsCreateRequest request) {
        Goods goods = new Goods();
        goods.setGoodsName(request.getGoodsName());
        goods.setGoodsType(request.getGoodsType());
        goods.setPrice(request.getPrice());
        goods.setInventory(request.getInventory());
        goods.setCoverUrl(request.getCoverUrl());
        goods.setColor(request.getColor());
        goods.setOrigin(request.getOrigin());
        goods.setShape(request.getShape());
        goods.setTransparency(request.getTransparency());
        goods.setVariety(request.getVariety());

        Goods saved = goodsService.create(goods);

        GoodsQueryResponse response = new GoodsQueryResponse();
        response.setSuccess(true);
        response.setGoods(GoodsConvertor.INSTANCE.mapToVo(saved));
        return response;
    }

    @Override
    public GoodsQueryResponse query(GoodsQueryRequest request) {
        Goods goods = goodsService.findByGoodsId(request.getGoodsId());

        GoodsQueryResponse response = new GoodsQueryResponse();
        response.setSuccess(true);
        if (goods != null) {
            response.setGoods(GoodsConvertor.INSTANCE.mapToVo(goods));
        }
        return response;
    }

    @Override
    public PageResponse<GoodsQueryResponse> pageQuery(GoodsPageQueryRequest request) {
        Page<Goods> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        Page<Goods> result = goodsService.pageQuery(page, request.getKeyword(), request.getGoodsType(),
                request.getColor(), request.getOrigin(), request.getShape(),
                request.getTransparency(), request.getVariety());

        List<GoodsVO> voList = GoodsConvertor.INSTANCE.mapToVo(result.getRecords());
        List<GoodsQueryResponse> responseList = voList.stream().map(vo -> {
            GoodsQueryResponse resp = new GoodsQueryResponse();
            resp.setSuccess(true);
            resp.setGoods(vo);
            return resp;
        }).toList();

        return PageResponse.of(responseList, (int) result.getTotal(), request.getPageSize(), request.getCurrentPage());
    }

    @Override
    @Facade
    public GoodsQueryResponse update(GoodsUpdateRequest request) {
        Goods goods = new Goods();
        goods.setGoodsId(request.getGoodsId());
        goods.setGoodsName(request.getGoodsName());
        goods.setGoodsType(request.getGoodsType());
        goods.setState(request.getState());
        goods.setPrice(request.getPrice());
        goods.setInventory(request.getInventory());
        goods.setCoverUrl(request.getCoverUrl());
        goods.setColor(request.getColor());
        goods.setOrigin(request.getOrigin());
        goods.setShape(request.getShape());
        goods.setTransparency(request.getTransparency());
        goods.setVariety(request.getVariety());

        Goods updated = goodsService.update(goods);

        GoodsQueryResponse response = new GoodsQueryResponse();
        if (updated != null) {
            response.setSuccess(true);
            response.setGoods(GoodsConvertor.INSTANCE.mapToVo(updated));
        } else {
            response.setSuccess(false);
            response.setResponseMessage("商品不存在");
        }
        return response;
    }

    @Override
    @Facade
    public GoodsQueryResponse delete(GoodsQueryRequest request) {
        boolean deleted = goodsService.deleteByGoodsId(request.getGoodsId());

        GoodsQueryResponse response = new GoodsQueryResponse();
        response.setSuccess(deleted);
        if (!deleted) {
            response.setResponseMessage("商品不存在或已删除");
        }
        return response;
    }
}
