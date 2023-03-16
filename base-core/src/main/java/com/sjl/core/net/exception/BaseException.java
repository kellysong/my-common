package com.sjl.core.net.exception;

import com.sjl.core.net.HttpErrorCode;

/**
 * 基类异常
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BaseException
 * @time 2020/9/21 10:27
 * @copyright(C) 2020 song
 */
public abstract class BaseException extends RuntimeException {
    public int errorCode = HttpErrorCode.ERROR_UNKNOWN;
    public String errorMsg = "";

    public BaseException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseException(String errorMsg, Throwable cause) {
        super(cause);
        this.errorMsg = errorMsg;
    }

    public BaseException(int errorCode, String errorMsg, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
