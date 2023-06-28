package com.sjl.core.util.log;

/**
 * 自定义轻量级写日志到文件工具类（目前可用于需要本地写日志的任何场景，其它不需要的场景使用控制台日志即可）
 *
 * @author songjiali
 * @version 4.1.0
 * @filename LogWriter.java
 * @time 2018/10/22 17:25
 * @copyright(C) 2018 song
 */
public class LogWriter {

    private static ILog log;

    private LogWriter() {

    }

    /**
     * 初始化LogWriter
     *
     * @param tag           标签
     * @param logDebugMode  日志打印标志，true打印日志，false不打印日志，包括写文件日志
     * @param writeFileFlag 写文件标志，默认false，false不写文件，true写文件
     * @see LogWriter#init(LogConfig)
     */
    @Deprecated
    public static void init(String tag, boolean logDebugMode, boolean writeFileFlag) {
        log = new DiskLog(tag, logDebugMode, writeFileFlag);
    }

    /**
     * 初始化LogWriter
     *
     * @param tag           标签
     * @param logDebugMode  日志打印标志，true打印日志，false不打印日志，包括写文件日志
     * @param writeFileFlag 写文件标志，默认false，false不写文件，true写文件
     * @param    logPath 日志路径
     * @see LogWriter#init(LogConfig)
     */
    @Deprecated
    public static void init(String tag, boolean logDebugMode, boolean writeFileFlag, String logPath) {
        log = new DiskLog(tag, logDebugMode, writeFileFlag,logPath);
    }

    /**
     * 初始化LogWriter
     *
     * @param logConfig 日志配置
     */
    public static void init(LogConfig logConfig) {
        log = new DiskLog(logConfig);
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
            throw new NullPointerException("LogWriter 未初始化.");
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
