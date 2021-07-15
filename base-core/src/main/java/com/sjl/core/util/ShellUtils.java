package com.sjl.core.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author Kelly
 */
public class ShellUtils {
    private static final String TAG = ShellUtils.class.getSimpleName();
    /**
     * root
     */
    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_LINE_END = "\n";

    /**
     * 执行单个命令
     *
     * @param cmd
     * @param sb
     * @param isRoot
     * @return
     */
    public static boolean exec(final String cmd, StringBuilder sb, boolean isRoot) {
        return batchExec(new String[]{cmd}, sb, isRoot);
    }

    /**
     * 执行单个命令
     *
     * @param cmd
     * @param isRoot
     * @return
     */
    public static boolean exec(final String cmd, boolean isRoot) {
        return exec(cmd, null, isRoot);
    }

    /**
     * 批量执行多个命令
     *
     * @param cmds
     * @param isRoot
     * @return
     */
    public static boolean batchExec(String[] cmds, boolean isRoot) {
        return batchExec(cmds, null, isRoot);
    }


    /**
     * 批量执行多个命令
     *
     * @param cmds
     * @param sb
     * @param isRoot
     * @return
     */
    public static boolean batchExec(String[] cmds, StringBuilder sb, boolean isRoot) {
        try {

            boolean ret = execShell(cmds, sb, isRoot);
            return ret;
        } catch (Exception e) {
            Log.e(TAG, "执行命令异常", e);
            return false;
        }
    }

    /**
     * 执行shell cmd
     * <p>
     * Linux 操作系统错误代码解释
     * *
     * OS error code 0: Success
     * <p>
     * 操作系统错误代码0：成功
     * <p>
     * OS error code 1: Operation not permitted
     * <p>
     * 操作系统错误代码1：操作不允许
     * <p>
     * OS error code 2: No such file or directory
     * <p>
     * 操作系统错误代码2：没有这样的文件或目录
     * <p>
     * OS error code 3: No such process
     * <p>
     * 操作系统错误代码3：没有这样的过程
     * <p>
     * OS error code 4: Interrupted system call
     * <p>
     * 操作系统错误代码4：中断的系统调用
     * <p>
     * OS error code 5: Input/output error
     * <p>
     * 操作系统错误代码5：输入/输出错误
     * <p>
     * ...
     *
     * @return
     */
    public static boolean execShell(String[] cmds, StringBuilder sb, boolean isRoot) throws Exception {
        Log.d(TAG, "exec shell:" + Arrays.toString(cmds));
        String result;
        DataOutputStream dataOutputStream;
        Process process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU + COMMAND_LINE_END : COMMAND_SH + COMMAND_LINE_END);
        OutputStream outputStream = process.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);

        for (String command : cmds) {
            if (command == null) continue;
            dataOutputStream.writeBytes(command + COMMAND_LINE_END);
            dataOutputStream.flush();
        }
        dataOutputStream.close();
        outputStream.close();
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        int read;
        char[] buffer = new char[4 * 1024];
        while ((read = reader.read(buffer)) > 0) {
            output.append(buffer, 0, read);
        }
        reader.close();

        if (output.length() > 0) {
            result = output.toString();
            Log.d(TAG, result);
            if (sb != null) {
                sb.append(result);
            }
        }

        int i = process.waitFor();
        Log.i(TAG, "waitFor i=" + i);
        process.destroy();
        return i == 0 ? true : false;
    }
}
