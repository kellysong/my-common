package com.sjl.core.util.log;

/**
 * 控制台日志输出工具类
 *
 * @author songjiali
 * @version 4.0.0
 * @filename LogUtils.java
 * @time 2018/10/22 17:25
 * @copyright(C) 2018 song
 */
public class LogUtils {
    private static ILog log;

    private LogUtils() {

    }

    /**
     * 设置日志过滤标签
     *
     * @param tag          标签
     * @param logDebugMode true打印日志，false不打印日志
     */
    public static void init(String tag, boolean logDebugMode) {
        log = new ConsoleLog(tag, logDebugMode);
    }

    /**
     * 以级别为 v 的形式输出LOG
     */

    public static void v(String msg) {
        checkLog();
        log.v(msg);
    }


    /**
     * 以级别为 d 的形式输出LOG
     */

    public static void d(String msg) {
        checkLog();
        log.d(msg);
    }

    /**
     * 以级别为 i 的形式输出LOG
     */

    public static void i(String msg) {
        checkLog();
        log.i(msg);
    }

    /**
     * 以级别为 w 的形式输出LOG
     */

    public static void w(String msg) {
        checkLog();
        log.w(msg);
    }

    /**
     * 以级别为 w 的形式输出Throwable
     */

    public static void w(Throwable tr) {
        checkLog();
        log.w(tr);
    }

    /**
     * 以级别为 w 的形式输出LOG信息和Throwable
     */

    public static void w(String msg, Throwable tr) {
        checkLog();
        log.w(msg, tr);
    }

    /**
     * 以级别为 e 的形式输出LOG
     */

    public static void e(String msg) {
        checkLog();
        log.e(msg);
    }

    /**
     * 以级别为 e 的形式输出Throwable
     */

    public static void e(Throwable tr) {
        checkLog();
        log.e(tr);
    }

    /**
     * 以级别为 e 的形式输出LOG信息和Throwable
     */

    public static void e(String msg, Throwable tr) {
        checkLog();
        log.e(msg, tr);
    }

    /**
     * 以级别为 e 的形式输出msg信息,附带时间戳，用于输出一个时间段结束点* @param msg 需要输出的msg
     */

    public static void elapsed(String msg) {
        checkLog();
        log.elapsed(msg);
    }

    private static void checkLog() {
        if (log == null) {
            log = new ConsoleLog(true);//初始化日志
        }
    }

    /**
     * 打印json
     */
    public static void json(String json) {
        checkLog();
        log.json(json);
    }

    /**
     *  打印xml
     */
    public static void xml(String xml) {
        checkLog();
        log.xml(xml);
    }

}
