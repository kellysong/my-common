package com.sjl.core.util.log;

import android.os.Environment;

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

import static com.sjl.core.util.log.ConsoleLog.LEVEL_ASSERT;
import static com.sjl.core.util.log.ConsoleLog.LEVEL_DEBUG;
import static com.sjl.core.util.log.ConsoleLog.LEVEL_ERROR;
import static com.sjl.core.util.log.ConsoleLog.LEVEL_INFO;
import static com.sjl.core.util.log.ConsoleLog.LEVEL_VERBOSE;
import static com.sjl.core.util.log.ConsoleLog.LEVEL_WARN;
import static com.sjl.core.util.log.ConsoleLog.LOG_STACK_TRACE_INDEX;
import static com.sjl.core.util.log.ConsoleLog.mTimestamp;

/**
 * 磁盘日志
 *
 * @author songjiali
 * @version 1.0.0
 * @filename DiskLog.java
 * @time 2018/11/1 10:19
 * @copyright(C) 2018 song
 */
public class DiskLog implements ILog {
    private ConsoleLog lightLog;
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
    private boolean isCanWrite;


    /**
     * 初始化initFileWriter
     *
     * @param tag           标签
     * @param logDebugMode  日志打印标志，true打印日志，false不打印日志，包括写文件日志
     * @param writeFileFlag 写文件标志，默认false，false不写文件，true写文件
     */
    public DiskLog(String tag, boolean logDebugMode, boolean writeFileFlag) {
        if (!logDebugMode) {
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
            } else {
                isCanWrite = true;
            }
        }
        deleteSDcardExpiredLog(logPath);
        lightLog = new ConsoleLog(tag, true);
        //纠正日志定位
        lightLog.setStackTraceIndex(LOG_STACK_TRACE_INDEX + 1);
    }

    @Override
    public void v(String msg) {
        lightLog.v(msg);
        writeLogToFile(LEVEL_VERBOSE, msg);

    }

    @Override
    public void d(String msg) {
        lightLog.d(msg);
        writeLogToFile(LEVEL_DEBUG, msg);
    }

    @Override
    public void i(String msg) {
        lightLog.i(msg);
        writeLogToFile(LEVEL_INFO, msg);

    }

    @Override
    public void w(String msg) {
        lightLog.w(msg);
        writeLogToFile(LEVEL_WARN, msg);
    }

    @Override
    public void w(Throwable tr) {
        lightLog.w(tr);
        writeLogToFile(LEVEL_WARN,null,tr);
    }

    @Override
    public void w(String msg, Throwable tr) {
        lightLog.w(msg, tr);
        writeLogToFile(LEVEL_WARN,msg,tr);
    }

    @Override
    public void e(String msg) {
        lightLog.e(msg);
        writeLogToFile(LEVEL_ERROR,msg,null);
    }

    @Override
    public void e(Throwable tr) {
        lightLog.e(tr);
        writeLogToFile(LEVEL_ERROR,null,tr);
    }

    @Override
    public void e(String msg, Throwable tr) {
        lightLog.e(msg, tr);
        writeLogToFile(LEVEL_ERROR,msg,tr);
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
        lightLog.wtf(msg);
        writeLogToFile(LEVEL_ASSERT,msg,null);
    }

    @Override
    public void json(String json) {
        lightLog.json(json);
        writeLogToFile(LEVEL_DEBUG,json,null);
    }

    @Override
    public void xml(String xml) {
        lightLog.xml(xml);
        writeLogToFile(LEVEL_DEBUG,xml,null);
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


    private void writeLogToFile(int logLevel, String msg) {
        if (!isCanWrite) {
            return;
        }
       writeLogToFile(logLevel,msg,null);
    }


    private void writeLogToFile(int logLevel, String msg,Throwable tr) {
        if (!isCanWrite) {
            return;
        }
        writeFile(logLevel,msg,tr);
    }


    private synchronized void writeFile(int logLevel, String msg,Throwable tr) {
        String logMsg = lightLog.createLog(msg,LOG_STACK_TRACE_INDEX);
        if (tr != null) {
            logMsg +=  "-->" + processCrashInfo(tr);
        }
        String content = appendDescribe(logLevel, logMsg);
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
                } catch (IOException e1) {

                }
            }
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
                .append(lightLog.getLogLevel(logLevel))
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
        if (tr == null) {
            return "";
        }
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
