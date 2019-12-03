package com.sjl.core.util.log;

import android.os.Environment;
import android.util.Log;

import com.sjl.core.app.BaseApplication;
import com.sjl.core.util.datetime.TimeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 磁盘日志
 *
 * @author songjiali
 * @version 1.0.0
 * @filename DiskLog.java
 * @time 2018/11/1 10:19
 * @copyright(C) 2018 song
 */
public class DiskLog extends LightLog {

    /**
     * 单个日志文件最大值 8M，理论上不会超出
     */
    private final int LOG_FILE_MAX_SIZE = 10 * 1024 * 1024;
    /**
     * 最多保存最近七天日志文件
     */
    private final int LOG_FILE_SAVE_DAYS = 7;

    /**
     * 日志文件
     */
    private File logFile;

    /**
     * 日志是否可写
     */
    private boolean isCanWrite = true;

    /**
     * 初始化initFileWriter
     *
     * @param logDebugMode  日志打印标志，true打印日志，false不打印日志，包括写文件日志
     * @param writeFileFlag 写文件标志，默认false，false不写文件，true写文件
     */
    public DiskLog(boolean logDebugMode, boolean writeFileFlag) {
        this(mTag, logDebugMode, writeFileFlag);
    }

    /**
     * 初始化initFileWriter
     *
     * @param tag           标签
     * @param logDebugMode  日志打印标志，true打印日志，false不打印日志，包括写文件日志
     * @param writeFileFlag 写文件标志，默认false，false不写文件，true写文件
     */
    public DiskLog(String tag, boolean logDebugMode, boolean writeFileFlag) {
        mTag = tag;
        if (!logDebugMode) {
            mDebuggable = LEVEL_NONE;
            isCanWrite = false;
            return;
        }
        isCanWrite = writeFileFlag;
        String logPath = Environment.getExternalStorageDirectory() + File.separator + BaseApplication.getContext().getPackageName() + File.separator + "Log";
        String logFileName = "log-" + TimeUtils.formatDateToStr(new Date(), TimeUtils.DATE_FORMAT_4) + ".txt";
        File fileDir = new File(logPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        logFile = new File(logPath + File.separator + logFileName);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            boolean ret = checkLogFileSize(logFile);
            if (ret) {//超出文件大小,不再写
                isCanWrite = false;
            }else{
                isCanWrite = true;
            }
        }
        deleteSDcardExpiredLog(logPath);
    }

    @Override
    public void v(String msg) {
        if (mDebuggable >= LEVEL_VERBOSE) {//5>1
            String logMsg = createLog(msg);
            Log.v(mTag, logMsg);
            if (isCanWrite) {
                writeLogToFile(appendDescribe(LEVEL_VERBOSE, logMsg));
            }
        }
    }

    @Override
    public void d(String msg) {
        if (mDebuggable >= LEVEL_DEBUG) {//5>2
            String logMsg = createLog(msg);
            Log.d(mTag, logMsg);
            if (isCanWrite) {
                writeLogToFile(appendDescribe(LEVEL_DEBUG, logMsg));
            }
        }
    }

    @Override
    public void i(String msg) {
        if (mDebuggable >= LEVEL_INFO) {//5>3
            String logMsg = createLog(msg);
            Log.i(mTag, logMsg);
            if (isCanWrite) {
                writeLogToFile(appendDescribe(LEVEL_INFO, logMsg));
            }
        }
    }

    @Override
    public void w(String msg) {
        if (mDebuggable >= LEVEL_WARN) {//5>4
            String logMsg = createLog(msg);
            Log.w(mTag, logMsg);
            if (isCanWrite) {
                writeLogToFile(appendDescribe(LEVEL_WARN, logMsg));
            }
        }
    }

    @Override
    public void w(Throwable tr) {
        if (mDebuggable >= LEVEL_WARN) {//5>4
            Log.w(mTag, "", tr);
            if (isCanWrite) {
                writeLogToFile(appendDescribe(LEVEL_WARN, "-->" + processCrashInfo(tr)));
            }
        }
    }

    @Override
    public void w(String msg, Throwable tr) {

        if (mDebuggable >= LEVEL_WARN && null != msg) {
            String logMsg = createLog(msg);
            Log.w(mTag, logMsg, tr);
            if (isCanWrite) {
                writeLogToFile(appendDescribe(LEVEL_WARN, logMsg + "-->" + processCrashInfo(tr)));
            }
        }
    }

    @Override
    public void e(String msg) {
        if (mDebuggable >= LEVEL_ERROR) {
            String logMsg = createLog(msg);
            Log.e(mTag, logMsg);
            if (isCanWrite) {
                writeLogToFile(appendDescribe(LEVEL_ERROR, logMsg));
            }
        }
    }

    @Override
    public void e(Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, "", tr);
            if (isCanWrite) {
                writeLogToFile(appendDescribe(LEVEL_ERROR, "-->" + processCrashInfo(tr)));
            }
        }
    }

    @Override
    public void e(String msg, Throwable tr) {

        if (mDebuggable >= LEVEL_ERROR && null != msg) {
            String logMsg = createLog(msg);
            Log.e(mTag, logMsg, tr);
            if (isCanWrite) {
                writeLogToFile(appendDescribe(LEVEL_ERROR, logMsg + "-->" + processCrashInfo(tr)));
            }
        }

    }

    @Override
    public void elapsed(String msg) {
        long currentTime = System.currentTimeMillis();

        long elapsedTime = currentTime - mTimestamp;

        mTimestamp = currentTime;

        e("[Elapsed：" + elapsedTime + "]" + msg);
    }

    /**
     * 删除sd卡下过期日志，只保留最近7份
     *
     * @param logPath 日志目录路径
     */
    private void deleteSDcardExpiredLog(String logPath) {
        File file = new File(logPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            orderByName(files);//升序
            int fileSize = files.length - LOG_FILE_SAVE_DAYS;
            for (int i = 0; i < fileSize; i++) {  //"-7"保存最近的7个日志文件
                File _file = files[i];
                _file.delete();
            }
        }
    }

    /**
     * 检查文件大小
     *
     * @param file 文件
     * @return
     */
    private boolean checkLogFileSize(File file) {
        if (file.length() > LOG_FILE_MAX_SIZE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 按照文件名称排序
     *
     * @param filesList 文件列表
     */
    private void orderByName(File[] filesList) {
        List files = Arrays.asList(filesList);
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    /**
     * 写日志内容到文件
     *
     * @param content 日志内容
     */
    private void writeLogToFile(String content) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(logFile, true);
            fileWriter.append(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e1) { /* TODO */ }
            }
        }
    }

    /**
     * 获取日志级别
     *
     * @param value
     * @return
     */
    private String getLogLevel(int value) {
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
            default:
                return "UNKNOWN";
        }
    }

    /**
     * 追加输出日志描述
     *
     * @param logLevel 日志级别
     * @param msg      日志信息
     * @return
     */
    private String appendDescribe(int logLevel, String msg) {
        return new StringBuilder().
                append(TimeUtils.formatDateToStr(new Date(), TimeUtils.DATE_FORMAT_6))
                .append("/")
                .append(getLogLevel(logLevel))
                .append("/")
                .append(msg)
                .append("\n").toString();
    }

    /**
     * 加工异常信息
     *
     * @param tr
     */
    private String processCrashInfo(Throwable tr) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        tr.printStackTrace(pw);
        Throwable cause = tr.getCause();
        while (cause != null) {// 循环着把所有的异常信息写入writer中
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.flush();
        pw.close();//关闭
        String result = writer.toString();
        return result;
    }

}
