package com.sjl.core.net.exception;

/**
 * token验证异常
 *
 * @author Kelly
 * @version 1.0.0
 * @filename TokenCheckException
 * @time 2020/9/13 20:18
 * @copyright(C) 2020 song
 */
public class TokenCheckException extends BaseException {


    public TokenCheckException(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public TokenCheckException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

    public TokenCheckException(int errorCode, String errorMsg, Throwable cause) {
        super(errorCode, errorMsg, cause);
    }
}
