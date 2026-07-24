package cn.hollis.nft.turbo.api.goods.constant;

/**
 * 商品状态
 */
public enum GoodsState {
    NOT_FOR_SALE,   // 不可售卖
    SELLING,        // 售卖中
    SOLD_OUT,       // 售空
    COMING_SOON,    // 即将开售
    WAIT_FOR_SALE   // 等待开售
}
