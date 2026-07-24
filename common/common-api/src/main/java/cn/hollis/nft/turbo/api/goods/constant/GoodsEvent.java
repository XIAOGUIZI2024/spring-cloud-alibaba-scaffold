package cn.hollis.nft.turbo.api.goods.constant;

/**
 * 商品事件（流水记录用）
 */
public enum GoodsEvent {
    CHAIN,              // 上链
    SALE,               // 出售
    CANCEL_SALE,        // 取消出售
    REMOVE,             // 下架
    MODIFY_INVENTORY,   // 修改库存
    MODIFY_PRICE        // 修改价格
}
