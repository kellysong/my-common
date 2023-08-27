package com.sjl.core.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Deflater是同时使用了LZ77算法与哈夫曼编码的一个无损数据压缩算法。
 *
 * @author Kelly
 * @version 1.0.0
 * @filename DataCompressUtils
 * @time 2022/11/20 14:06
 */
public class DataCompressUtils {

    /**
     * 压缩
     *
     * @param input
     * @return
     */
    public static byte[] compress(byte[] input) throws Exception {
        try {
            Deflater deflater = new Deflater();
            deflater.setInput(input);
            deflater.finish();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
            byte[] buf = new byte[2048];
            while (!deflater.finished()) {
                int byteCount = deflater.deflate(buf);
                outputStream.write(buf, 0, byteCount);
            }
            deflater.end();
            byte[] originalBytes = outputStream.toByteArray();
            return originalBytes;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 解压缩
     *
     * @param input
     * @return
     * @throws Exception
     */
    public static byte[] uncompress(byte[] input) throws Exception {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(input);
            byte[] buf = new byte[2048];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
            while (!inflater.finished()) {
                int length = inflater.inflate(buf);
                outputStream.write(buf, 0, length);
            }
            inflater.end();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }
}
