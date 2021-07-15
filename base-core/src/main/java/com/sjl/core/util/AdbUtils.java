package com.sjl.core.util;


import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * adb工具类
 *
 * 查看APP的报错日志，信息，截图
 *
 * @author Kelly
 */
public class AdbUtils {

    private static final String TAG = AdbUtils.class.getSimpleName();


    /***
     * 抓取应用错误日志
     * @param context
     * @param sb
     */
    public static boolean pullLogCatErrorLog(Context context, StringBuilder sb) {
        //app的pid
        int pid = android.os.Process.myPid();
        Log.d(TAG, "pid" + pid);
        String cmd = "logcat -d '*:E' | grep " + pid;
        boolean ret = ShellUtils.exec(cmd, sb, false);
        Log.d(TAG, "pullLogCatErrorLog end. " + context.getPackageName());
        return ret;
    }

    /**
     * 拉取logcat I级别以下日志,并保存到文件里
     * <p>
     * 以pid筛选
     * log -d 不阻塞 -c 清空缓存log
     * grep -v  chatty 筛选（不要的chatty）
     * </p>
     *
     * @param context
     * @return
     */
    public static boolean pullLogCatLog(Context context) {
        File out = createLogFile(context, "logcat.txt");
        //app的pid
        int pid = android.os.Process.myPid();
        /*       String pidStr = ShellUtils.exec("pidof " + packageName,false);
        int pid = -1;
        try {
            pid = Integer.parseInt(pidStr.substring(0, pidStr.length() - 1));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }*/
        Log.d(TAG, "pid" + pid);
        String cmd = "logcat -d '*:I' | grep " + pid + "  >" + out.getAbsolutePath();
        boolean ret = ShellUtils.exec(cmd, false);
        Log.d(TAG, "pullLogCatLog end. " + context.getPackageName());
        return ret;
    }


    /**
     * 清除logcat日志
     */
    public static void clearLogCatLog() {
        String cmd = "logcat -c \n";
        ShellUtils.exec(cmd, false);
    }

    /**
     * 清除文件
     */
    public static void clearFiles(Context context) {
        File out = getLogDir(context);
        File[] files = out.listFiles();
        if (files == null || files.length == 0) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    /**
     * 拉去应用anr日志
     * <p>adb -s CBI9SD7Q67 pull /data/anr C:\log 有些机型不行</p>
     * <p>需要root权限，慎用</p>
     *
     * @param context
     * @return
     */
    public static boolean pullAnrLog(Context context) {
        File out = getLogDir(context);
        String cmd = "ls /data/anr/ ";
        StringBuilder r = new StringBuilder();
        ShellUtils.exec(cmd, r, false);
        if (r.length() > 0) {//检查是否存在文件
            String s = r.toString().trim();
            String[] files = s.split("\n");
            if (files == null) {
                return false;
            }
            r.setLength(0);
            for (String filename : files) {
                String outFile = out.getAbsolutePath() + File.separator + filename;
                String[] cmds = new String[]{
//                        "ls /data/anr",
                        "cat /data/anr/" + filename + " >" + outFile,
                        "exit",
                        "pull " + outFile
                };
                return ShellUtils.batchExec(cmds, r, true);
//                writeFile(r.toString(),new File(outFile));
            }

        }
        return false;

    }

    /**
     * 抓取应用fd信息并导出到指定文件，分析fd泄露导致应用崩溃的原因
     * <p>需要root权限，慎用</p>
     *
     * @param context
     */
    public static boolean pullFdInfo(Context context) {
        File out = createLogFile(context, "fd.txt");
        int pid = android.os.Process.myPid();
        String[] cmds = new String[]{
                "ls -l /proc/" + pid + "/fd",
                "exit"};
        StringBuilder sb = new StringBuilder();
        boolean b = ShellUtils.batchExec(cmds, sb, true);
        if (b) {
            writeFile(sb.toString(), out, true);
        }
        return b;
    }

    /**
     * 抓取应用信息并导出到指定文件
     * <p>需要root权限，慎用</p>
     *
     * @param context
     */
    public static boolean pullApkInfo(Context context) {
        File out = createLogFile(context, "apko-info.txt");
        String cmd = "dumpsys package " + context.getPackageName();
        StringBuilder sb = new StringBuilder();
        boolean b = ShellUtils.exec(cmd, sb, true);
        if (b) {
            writeFile(sb.toString(), out);
        }
        return b;
    }

    /**
     * 屏幕截图
     *
     * @param context
     * @return
     */
    public static boolean getScreenshots(Context context) {
        File out = createLogFile(context, "screenshots.png");
        String cmd = "screencap  " + out.getAbsolutePath();
        boolean b = ShellUtils.exec(cmd, true);
        return b;
    }


    private static void writeFile(String content, File file) {
        writeFile(content, file, false);
    }

    private static void writeFile(String content, File file, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, append);
            fileWriter.append(content);
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                }
            }
        }
    }


    private static File createLogFile(Context context, String filename) {
        File logDir = getLogDir(context);
        File outFile = new File(logDir, filename);
        if (!outFile.exists()) {
            try {
                outFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outFile;
    }

    private static File getLogDir(Context context) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String packageName = context.getPackageName();
        File dir = new File(externalStorageDirectory, packageName + File.separator + "adb" + File.separator);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }


}