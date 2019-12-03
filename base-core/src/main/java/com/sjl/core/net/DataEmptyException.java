package com.sjl.core.net;

/**
 * 自定义数据空异常
 *
 * @author Kelly
 * @version 1.0.0
 * @filename DataEmptyException.java
 * @time 2019/5/7 15:35
 * @copyright(C) 2019 song
 */
public class DataEmptyException extends RuntimeException {

    public DataEmptyException() {
    }

    public DataEmptyException(String message) {
        super(message);
    }

    public DataEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataEmptyException(Throwable cause) {
        super(cause);
    }

}
