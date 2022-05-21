package com.sjl.lib.net;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename DataResponseException
 * @time 2020/9/14 10:27
 * @copyright(C) 2020 song
 */
public class DataResponseException extends RuntimeException {
    public int code;
    public DataResponseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public DataResponseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
