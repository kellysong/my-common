package com.sjl.core.util.log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * 第三方日志库封装
 *
 * @author Kelly
 * @version 1.0.0
 * @filename LoggerUtils.java
 * @time 2018/8/3 8:50
 * @copyright(C) 2018 song
 */
public class LoggerUtils {
    /**
     * 初始化log工具，在app入口处调用
     *
     * @param tag         日志tag
     * @param isLogEnable 是否打印log
     */
    public static void init(String tag, final boolean isLogEnable) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  //（可选）是否显示线程信息。 默认值为true
                .methodCount(2)         // （可选）要显示的方法行数。 默认2
                .methodOffset(5)        // （可选）隐藏内部方法调用到偏移量。 默认5
                .logStrategy(new LogcatLogStrategy()) //（可选）更改要打印的日志策略。 默认LogCat
                .tag(tag)   //（可选）每个日志的全局标记。 默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return isLogEnable;
            }
        });//初始化日志适配器
    }

    public static void d(String message) {
        Logger.d(message);
    }

    public static void i(String message) {
        Logger.i(message);
    }

    public static void w(String message, Throwable e) {
        String info = e != null ? e.toString() : "null";
        Logger.w(message + "：" + info);
    }

    public static void e(String message, Throwable e) {
        Logger.e(e, message);
    }

    public static void json(String json) {
        Logger.json(json);
    }
}
