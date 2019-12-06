package com.sjl.core.net;

/**
 * 网络请求日志适配器，手动控制
 *
 * @author Kelly
 * @version 1.0.0
 * @filename RetrofitLogAdapter.java
 * @time 2019/12/4 9:07
 * @copyright(C) 2019 song
 */
public interface RetrofitLogAdapter {
    /**
     * 打印请求路径
     *
     * @return
     */
    boolean printRequestUrl();

    /**
     * 打印详细http日志
     *
     * @return
     */
    boolean printHttpLog();
}
