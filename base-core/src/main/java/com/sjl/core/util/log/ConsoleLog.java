package com.sjl.core.util.log;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 轻量级控制台日志封装
 *
 * @author songjiali
 * @version 1.0.0
 * @filename ConsoleLog.java
 * @time 2018/11/1 10:09
 * @copyright(C) 2018 song
 */
public  class ConsoleLog implements ILog {
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

    public static final int LEVEL_VERBOSE = 2;

    /**
     * 日志输出级别D
     */

    public static final int LEVEL_DEBUG = 3;

    /**
     * 日志输出级别I
     */

    public static final int LEVEL_INFO = 4;

    /**
     * 日志输出级别W
     */

    public static final int LEVEL_WARN = 5;

    /**
     * 日志输出级别E
     */

    public static final int LEVEL_ERROR = 6;

    public static final int LEVEL_ASSERT = 7;
    /**
     * 是否允许输出log,不能用静态
     */

    protected int mDebuggable = LEVEL_ASSERT;//0不输出

    /**
     * 用于记时的变量
     */

    public static long mTimestamp = 0;

    /**
     * 该参数需要根据实际情况来设置,才能准确获取期望的调用信息,默认8
     */
    protected static final int LOG_STACK_TRACE_INDEX = 8;

    /**
     * It is used for json pretty print
     */
    protected static final int JSON_INDENT = 2;

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;
    private int stackTraceIndex;

    public void setStackTraceIndex(int stackTraceIndex) {
        this.stackTraceIndex = stackTraceIndex;
    }

    /**
     * 设置日志过滤标签
     *
     * @param logDebugMode true打印日志，false不打印日志
     */
    public ConsoleLog(boolean logDebugMode) {
        this(mTag, logDebugMode);
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
        stackTraceIndex = LOG_STACK_TRACE_INDEX;
    }


    @Override
    public void v(String msg) {
        if (mDebuggable >= LEVEL_VERBOSE) {
            printLog(LEVEL_VERBOSE, msg);
        }
    }

    @Override
    public void d(String msg) {
        if (mDebuggable >= LEVEL_DEBUG) {
            printLog(LEVEL_DEBUG, msg);
        }
    }


    @Override
    public void i(String msg) {
        if (mDebuggable >= LEVEL_INFO) {
            printLog(LEVEL_INFO, msg);
        }
    }

    @Override
    public void w(String msg) {
        if (mDebuggable >= LEVEL_WARN) {
            printLog(LEVEL_WARN, msg);

        }
    }

    @Override
    public void w(Throwable tr) {
        if (mDebuggable >= LEVEL_WARN) {
            printLog(LEVEL_WARN, null, tr);
        }
    }

    @Override
    public void w(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_WARN && null != msg) {
            printLog(LEVEL_WARN, msg, tr);
        }

    }

    @Override
    public void e(String msg) {
        if (mDebuggable >= LEVEL_ERROR) {
            printLog(LEVEL_ERROR, msg);
        }
    }

    @Override
    public void e(Throwable tr) {

        if (mDebuggable >= LEVEL_ERROR) {
            printLog(LEVEL_ERROR, null, tr);

        }
    }

    @Override
    public void e(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR && null != msg) {
            printLog(LEVEL_ERROR, msg, tr);

        }
    }

    @Override
    public void elapsed(String msg) {
        long currentTime = System.currentTimeMillis();

        long elapsedTime = currentTime - mTimestamp;

        mTimestamp = currentTime;

        e("[Elapsed：" + elapsedTime + "]" + msg);
    }

    @Override
    public void wtf(String msg) {
        if (mDebuggable >= LEVEL_ASSERT) {
            printLog(LEVEL_ASSERT, msg);
        }
    }

    @Override
    public void json(String json) {
        if (mDebuggable == LEVEL_NONE) {
            return;
        }
        if (TextUtils.isEmpty(json)) {
            printLog(LEVEL_DEBUG, "Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                printLog(LEVEL_DEBUG, message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                printLog(LEVEL_DEBUG, message);
                return;
            }
            printLog(LEVEL_ERROR, "Invalid Json");
        } catch (JSONException e) {
            printLog(LEVEL_ERROR, "Invalid Json");
        }
    }

    @Override
    public void xml(String xml) {
        if (mDebuggable == LEVEL_NONE) {
            return;
        }
        if (TextUtils.isEmpty(xml)) {
            printLog(LEVEL_DEBUG, "Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            printLog(LEVEL_DEBUG, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            printLog(LEVEL_ERROR, "Invalid xml");
        }
    }


    /**
     * 获取日志级别
     *
     * @param value
     * @return
     */
    protected String getLogLevel(int value) {
        switch (value) {
            case LEVEL_VERBOSE:
                return "V";
            case LEVEL_DEBUG:
                return "D";
            case LEVEL_INFO:
                return "I";
            case LEVEL_WARN:
                return "W";
            case LEVEL_ERROR:
                return "E";
            case LEVEL_ASSERT:
                return "A";
            default:
                return "UNKNOWN";
        }
    }



    /**
     * 创建日志描述
     *
     * @param msg
     * @return
     */
    protected String createLog(String msg) {
        return createLog(msg,stackTraceIndex);
    }
    protected String createLog(String msg,int stackTraceIndex) {
        StringBuilder builder = new StringBuilder();
        Thread thread = Thread.currentThread();
        StackTraceElement[]  stackTrace = thread.getStackTrace();
        StackTraceElement ste = stackTrace[stackTraceIndex];
        String className = ste.getFileName();
        String methodName = ste.getMethodName();
        int lineNumber = ste.getLineNumber();
        builder.append(methodName);
        builder.append("(").append(className).append(":").append(lineNumber).append(")");
        builder.append(msg);
        builder.append("  ---->").append("Thread:").append(thread.getName());
        return builder.toString();
    }

    public void printLog(int logLevel, String msg) {
        println(logLevel, msg, null);
    }

    public void printLog(int logLevel, String msg, Throwable tr) {
        println(logLevel, msg, tr);
    }


    /**
     * 同步打印，防止错乱
     *
     * @param logLevel
     * @param msg
     * @param tr
     */
    private synchronized void println(int logLevel, String msg, Throwable tr) {
        if (tr != null && msg != null) {
            msg += " : " + Log.getStackTraceString(tr);
        }
        if (tr != null && msg == null) {
            msg = Log.getStackTraceString(tr);
        }
        final String log = createLog(msg);
        byte[] bytes = log.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            Log.println(logLevel, mTag, log);
            return;
        }

        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            //UTF-8 for Android
            Log.println(logLevel, mTag, new String(bytes, i, count));
        }
    }

}

