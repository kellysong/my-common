package com.sjl.core.util.log;

import android.util.Log;

/**
 * 控制台日志
 *
 * @author songjiali
 * @version 1.0.0
 * @filename ConsoleLog.java
 * @time 2018/11/1 10:13
 * @copyright(C) 2018 song
 */
public class ConsoleLog extends LightLog {


    /**
     * 设置日志过滤标签
     *
     * @param logDebugMode true打印日志，false不打印日志
     */
    public ConsoleLog(boolean logDebugMode) {
        this(mTag,logDebugMode);
    }

    /**
     * 设置日志过滤标签
     *
     * @param tag          标签
     * @param logDebugMode true打印日志，false不打印日志
     */
    public ConsoleLog(String tag, boolean logDebugMode) {
        mTag = tag;
        if (!logDebugMode) {
            mDebuggable = LEVEL_NONE;
        }
    }


    @Override
    public void v(String msg) {
        if (mDebuggable >= LEVEL_VERBOSE) {//5>1
            Log.v(mTag, createLog(msg));
        }
    }

    @Override
    public void d(String msg) {
        if (mDebuggable >= LEVEL_DEBUG) {//5>2

            Log.d(mTag, createLog(msg));

        }
    }

    @Override
    public void i(String msg) {
        if (mDebuggable >= LEVEL_INFO) {//5>3

            Log.i(mTag, createLog(msg));

        }
    }

    @Override
    public void w(String msg) {
        if (mDebuggable >= LEVEL_WARN) {//5>4

            Log.w(mTag, createLog(msg));

        }
    }

    @Override
    public void w(Throwable tr) {
        if (mDebuggable >= LEVEL_WARN) {//5>4

            Log.w(mTag, "", tr);

        }
    }

    @Override
    public void w(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_WARN && null != msg) {

            Log.w(mTag, createLog(msg), tr);
        }

    }

    @Override
    public void e(String msg) {
        if (mDebuggable >= LEVEL_ERROR) {

            Log.e(mTag, createLog(msg));

        }
    }

    @Override
    public void e(Throwable tr) {

        if (mDebuggable >= LEVEL_ERROR) {

            Log.e(mTag, "", tr);

        }
    }

    @Override
    public void e(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR && null != msg) {

            Log.e(mTag, createLog(msg), tr);

        }
    }

    @Override
    public void elapsed(String msg) {
        long currentTime = System.currentTimeMillis();

        long elapsedTime = currentTime - mTimestamp;

        mTimestamp = currentTime;

        e("[Elapsed：" + elapsedTime + "]" + msg);
    }
}
