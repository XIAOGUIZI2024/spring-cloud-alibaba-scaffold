package cn.hollis.nft.turbo.goods.infrastructure.exception;

import cn.hollis.nft.turbo.base.exception.ErrorCode;

/**
 * 商品错误码
 */
public enum GoodsErrorCode implements ErrorCode {

    GOODS_NOT_EXIST("GOODS_NOT_EXIST", "商品不存在"),
    GOODS_CREATE_FAILED("GOODS_CREATE_FAILED", "商品创建失败"),
    GOODS_UPDATE_FAILED("GOODS_UPDATE_FAILED", "商品更新失败"),
    INVENTORY_NOT_ENOUGH("INVENTORY_NOT_ENOUGH", "库存不足"),
    GOODS_NOT_FOR_SALE("GOODS_NOT_FOR_SALE", "商品不可售卖"),
    GOODS_OPERATE_FAILED("GOODS_OPERATE_FAILED", "商品操作失败");

    private String code;
    private String message;

    GoodsErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
