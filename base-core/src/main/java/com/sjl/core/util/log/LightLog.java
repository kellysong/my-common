package com.sjl.core.util.log;

/**
 * 抽象日志类
 *
 * @author songjiali
 * @version 1.0.0
 * @filename LightLog.java
 * @time 2018/11/1 10:09
 * @copyright(C) 2018 song
 */
public abstract class LightLog {
    /**
     * 日志输出时的TAG
     */

    public static String mTag = "SIMPLE_LOGGER";

    /**
     * 日志输出级别NONE
     */

    public static final int LEVEL_NONE = 0;

    /**
     * 日志输出级别V
     */

    public static final int LEVEL_VERBOSE = 1;

    /**
     * 日志输出级别D
     */

    public static final int LEVEL_DEBUG = 2;

    /**
     * 日志输出级别I
     */

    public static final int LEVEL_INFO = 3;

    /**
     * 日志输出级别W
     */

    public static final int LEVEL_WARN = 4;

    /**
     * 日志输出级别E
     */

    public static final int LEVEL_ERROR = 5;

    /**
     * 是否允许输出log,不能用静态
     */

    protected int mDebuggable = LEVEL_ERROR;//0不输出

    /**
     * 用于记时的变量
     */

    public static long mTimestamp = 0;

    /**
     * 该参数需要根据实际情况来设置（日志类继承深度，没有继承的话是4）才能准确获取期望的调用信息
     */
    private static final int LOG_STACK_TRACE_INDEX = 5;


    /**
     * 以级别为 v 的形式输出LOG
     */

    public abstract void v(String msg);

    /**
     * 以级别为 d 的形式输出LOG
     */

    public abstract void d(String msg);

    /**
     * 以级别为 i 的形式输出LOG
     */

    public abstract void i(String msg);

    /**
     * 以级别为 w 的形式输出LOG
     */
    public abstract void w(String msg);

    /**
     * 以级别为 w 的形式输出Throwable
     */
    public abstract void w(Throwable tr);

    /**
     * 以级别为 w 的形式输出LOG信息和Throwable
     */
    public abstract void w(String msg, Throwable tr);

    /**
     * 以级别为 e 的形式输出LOG
     */

    public abstract void e(String msg);

    /**
     * 以级别为 e 的形式输出Throwable
     */

    public abstract void e(Throwable tr);

    /**
     * 以级别为 e 的形式输出LOG信息和Throwable
     */

    public abstract void e(String msg, Throwable tr);

    /**
     * 以级别为 e 的形式输出msg信息,附带时间戳，用于输出一个时间段结束点* @param msg 需要输出的msg
     */

    public abstract void elapsed(String msg);

    /**
     * 创建日志描述
     *
     * @param msg
     * @return
     */
    protected String createLog(String msg) {
        StringBuilder builder = new StringBuilder();
        Thread thread = Thread.currentThread();
        StackTraceElement[] stackTrace = thread.getStackTrace();
        String className = stackTrace[LOG_STACK_TRACE_INDEX].getFileName();
        String methodName = stackTrace[LOG_STACK_TRACE_INDEX].getMethodName();
        int lineNumber = stackTrace[LOG_STACK_TRACE_INDEX].getLineNumber();
        builder.append(methodName);
        builder.append("(").append(className).append(":").append(lineNumber).append(")");
        builder.append(msg);
        builder.append("  ---->").append("Thread:").append(thread.getName());
        return builder.toString();
    }
}

