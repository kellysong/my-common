package com.sjl.core.net;

/**
 * http错误码
 *
 */
public interface HttpErrorCode {
    /**
     * 网络不可用
     */
    int ERROR_NETWORK_UNAVAILABLE = -101;
    /**
     * 网络错误
     */
     int ERROR_NETWORK_BAD  = -102;

    /**
     * 协议出错
     */
     int ERROR_HTTP_ERROR = -103;

    /**
     * 证书出错
     */
    int ERROR_SSL = -104;

    /**
     * 解析错误
     */
    int ERROR_PARSE = -105;

    /**
     * 转换错误
     */
    int ERROR_TRANSFORMER= -106;

    /**
     * 未知错误
     */
     int ERROR_UNKNOWN = -999;


}
