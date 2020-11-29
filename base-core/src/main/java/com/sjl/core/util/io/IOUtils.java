package com.sjl.core.util.io;

import com.sjl.core.util.file.CharsetEnum;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename IOUtils
 * @time 2020/11/29 13:16
 * @copyright(C) 2020 song
 */
public class IOUtils {
    private static final int EOF = -1;
    /**
     * The Unix directory separator character.
     */
    public static final char DIR_SEPARATOR_UNIX = '/';
    /**
     * The Windows directory separator character.
     */
    public static final char DIR_SEPARATOR_WINDOWS = '\\';
    /**
     * The system directory separator character.
     */
    public static final char DIR_SEPARATOR = File.separatorChar;
    /**
     * The Unix line separator string.
     */
    public static final String LINE_SEPARATOR_UNIX = "\n";
    /**
     * The Windows line separator string.
     */
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * 关闭流
     * @param c  InputStream,OutputStream,Reader,Writer,Selector，Socket等
     */
    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
                //  ignored
            }
        }
    }

    /**
     * Closes a URLConnection.
     * @param conn
     */
    public static void close(URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    /**
     * 输入流转成字符串
     * @param input
     * @return
     * @throws IOException
     */
    public static String toString(InputStream input) throws IOException {
        return toString(input, java.nio.charset.Charset.defaultCharset());
    }

    /**
     * 根据指定编码把输入流转成字符串
     * @param input
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String toString(InputStream input, java.nio.charset.Charset encoding) throws IOException {
        //处理流
        BufferedReader bufferedReader = null;
        try {
            InputStreamReader   read = new InputStreamReader(input, CharsetEnum.toCharset(encoding));
            bufferedReader = new BufferedReader(read);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);

            }
            return sb.toString();
        }finally {
            close(bufferedReader);
        }
    }

    /**
     * 输入流转字数组
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copyStream(input, output, new byte[DEFAULT_BUFFER_SIZE]);
        output.close();
        return output.toByteArray();
    }

    /**
     * 拷贝流
      * @param input
     * @param output
     * @param buffer
     * @return
     * @throws IOException
     */
    public static long copyStream(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
