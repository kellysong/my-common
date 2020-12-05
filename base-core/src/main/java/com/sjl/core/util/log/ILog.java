package com.sjl.core.util.log;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ILog
 * @time 2020/12/5 17:07
 * @copyright(C) 2020 song
 */
public interface ILog {

    /**
     * 以级别为 v 的形式输出LOG
     */
    void v(String msg);

    /**
     * 以级别为 d 的形式输出LOG
     */
    void d(String msg);

    /**
     * 以级别为 i 的形式输出LOG
     */
    void i(String msg);

    /**
     * 以级别为 w 的形式输出LOG
     */
    void w(String msg);

    /**
     * 以级别为 w 的形式输出Throwable
     */
    void w(Throwable tr);

    /**
     * 以级别为 w 的形式输出LOG信息和Throwable
     */
    void w(String msg, Throwable tr);

    /**
     * 以级别为 e 的形式输出LOG
     */
    void e(String msg);

    /**
     * 以级别为 e 的形式输出Throwable
     */
    void e(Throwable tr);

    /**
     * 以级别为 e 的形式输出LOG信息和Throwable
     */
    void e(String msg, Throwable tr);

    /**
     * 以级别为 e 的形式输出msg信息,附带时间戳，用于输出一个时间段结束点* @param msg 需要输出的msg
     */
    void elapsed(String msg);

    /**
     * assert日志
     *
     * @param msg
     */
    void wtf(String msg);

    /**
     * 以级别为 d的形式输出json
     *
     * @param json
     */
    void json(String json);

    /**
     * 以级别为 d的形式输出xml
     *
     * @param xml
     */
    void xml(String xml);
}
