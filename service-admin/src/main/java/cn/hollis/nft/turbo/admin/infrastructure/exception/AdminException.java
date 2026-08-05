package cn.hollis.nft.turbo.admin.infrastructure.exception;

import cn.hollis.nft.turbo.base.exception.BizException;
import cn.hollis.nft.turbo.base.exception.ErrorCode;

/**
 * 管理后台异常
 *
 * @author DaDagui
 */
public class AdminException extends BizException {

    public AdminException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AdminException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
