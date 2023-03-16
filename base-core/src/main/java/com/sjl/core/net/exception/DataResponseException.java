package com.sjl.core.net.exception;

/**
 * 数据响应异常
 *
 * @author Kelly
 * @version 1.0.0
 * @filename DataResponseException
 * @time 2020/9/14 10:27
 * @copyright(C) 2020 song
 */
public class DataResponseException extends BaseException {


    public DataResponseException(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public DataResponseException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

    public DataResponseException(int errorCode, String errorMsg, Throwable cause) {
        super(errorCode, errorMsg, cause);
    }
}
