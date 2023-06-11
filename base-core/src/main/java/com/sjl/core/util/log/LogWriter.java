package com.sjl.core.util.log;

/**
 * 自定义轻量级写日志到文件工具类（目前只用于设备驱动，网络通信，其它不需要，控制台日志即可）
 *
 * @author songjiali
 * @version 4.0.0
 * @filename LogWriter.java
 * @time 2018/10/22 17:25
 * @copyright(C) 2018 song
 */
public class LogWriter {

    private static ILog log;

    private LogWriter() {

    }

    /**
     * 初始化initFileWriter
     *
     * @param tag           标签
     * @param logDebugMode  日志打印标志，true打印日志，false不打印日志，包括写文件日志
     * @param writeFileFlag 写文件标志，默认false，false不写文件，true写文件
     */
    public static void init(String tag, boolean logDebugMode, boolean writeFileFlag) {
        log = new DiskLog(tag, logDebugMode, writeFileFlag);
    }

    /**
     * 初始化initFileWriter
     *
     * @param tag           标签
     * @param logDebugMode  日志打印标志，true打印日志，false不打印日志，包括写文件日志
     * @param writeFileFlag 写文件标志，默认false，false不写文件，true写文件
     * @param    logPath 日志路径
     */
    public static void init(String tag, boolean logDebugMode, boolean writeFileFlag, String logPath) {
        log = new DiskLog(tag, logDebugMode, writeFileFlag,logPath);
    }
    /**
     * 设置文件保留天数
     *
     * @param saveDay
     */
    public static void setSaveDay(int saveDay) {
        checkLog();
        final DiskLog diskLog = (DiskLog) log;
        diskLog.setSaveDay(saveDay);
    }


    /**
     * 设置单个文件大小
     *
     * @param singleFileSize
     */
    public static void setSingleFileSize(int singleFileSize) {
        checkLog();
        final DiskLog diskLog = (DiskLog) log;
        diskLog.setSingleFileSize(singleFileSize);
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
            log = new DiskLog(ConsoleLog.mTag,true, true);//初始化，如果需要开启写磁盘，调用init初始化
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
