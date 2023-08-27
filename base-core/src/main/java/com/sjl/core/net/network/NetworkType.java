package com.sjl.core.net.network;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename NetworkType
 * @time 2023/6/27 14:54
 * @copyright(C) 2023 song
 */
public interface NetworkType {
    /**
     * 移动
     */
    int MOBILE = 0;

    /**
     * wifi
     */
    int WIFI = 1;

    /**
     * 以太网
     */
    int ETHERNET = 2;

    /**
     * 其它
     */
    int OTHER = 3;
}
