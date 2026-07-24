package cn.hollis.nft.turbo.goods.infrastructure.exception;

import cn.hollis.nft.turbo.base.exception.BizException;
import cn.hollis.nft.turbo.base.exception.ErrorCode;

/**
 * 商品异常
 */
public class GoodsException extends BizException {

    public GoodsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public GoodsException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
