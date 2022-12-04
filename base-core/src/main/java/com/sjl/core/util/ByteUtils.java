package com.sjl.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 字节操作工具类
 *
 * @author songjiali
 * @version 1.0.0
 * @filename ByteUtils.java
 * @time 2018/10/22 17:25
 * @copyright(C) 2018 song
 */
public class ByteUtils {

    /**
     * 16进制字符串转字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByteArr(String hex) {
        int l = hex.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer.valueOf(hex.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteArrToHexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString().toUpperCase();
    }

    /**
     * 字节转16进制字符串
     *
     * @param input
     * @param offset 偏移索引
     * @param size
     * @return
     */
    public static String byteToHex(byte[] input, final int offset, final int size) {
        if (input == null)
            return "";
        StringBuilder sb = new StringBuilder();
        int i;
        for (int d = offset; d < offset + size; d++) {
            i = input[d];
            if (i < 0)
                i += 256;
            if (i < 16)
                sb.append("0");
            sb.append(Integer.toString(i, 16));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 将字符串转成ASCII
     *
     * @param str
     * @return
     */
    public static String stringToAscii(String str) {
        StringBuffer sb = new StringBuffer();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sb.append((int) chars[i]);
        }
        return sb.toString();
    }

    /**
     * 将ASCII转成字符串
     *
     * @param ascii
     * @return
     */
    public static String asciiToString(String ascii) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = ascii.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) chars[i]);
        }
        return sbu.toString();
    }

    /**
     * ASCII码转换为16进制
     * @param ascii
     * @return
     */
    public static String  asciiToToHex(String ascii){

        char[] chars = ascii.toCharArray();

        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();
    }

    /**
     * 16进制转换为ASCII
     * @param hex
     * @return
     */
    public static String hexToAscii(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }
    /**
     * 调整字节数组
     *
     * @param response
     * @param length
     */
    public static byte[] adjustByteArray(byte[] response, int length) {
        byte[] actual = new byte[length];
        System.arraycopy(response, 0, actual, 0, length);
        return actual;
    }

    /**
     * 获取高四位
     *
     * @param data
     * @return
     */
    public static int getHeight4(byte data) {
        int height;
        height = ((data & 0xf0) >> 4);
        return height;
    }

    /**
     * 取出二进制的各位值、
     * 比如：10011
     * 1	1	0	0	1
     *
     * @param num:要获取二进制值的数
     * @param index:倒数第一位为0，依次类推
     */
    public static int getBitValue(int num, int index) {
        return (num & (0x1 << index)) >> index;
    }

    /**
     * 获取低四位
     *
     * @param data
     * @return
     */
    public static int getLow4(byte data) {
        int low;
        low = (data & 0x0f);
        return low;
    }

    /**
     * 字节传字符串
     *
     * @param inBytArr
     * @param offset
     * @param byteCount
     * @return
     */
    public static String byteToString(byte[] inBytArr, int offset, int byteCount) {
        byte[] str = new byte[byteCount];
        System.arraycopy(inBytArr, offset, str, 0, byteCount);
        String res = new String(str);
        return res;
    }

    /**
     * 字节传字符串
     *
     * @param inBytArr
     * @return
     */
    public static String byteToString(byte[] inBytArr) {
        String res = new String(inBytArr);
        return res;
    }


    /**
     * 使用1字节就可以表示b
     *
     * @param b
     * @return
     */
    public static String numToHex8(int b) {
        return String.format("%02x", b);// 2表示需要两个数表示16进制
    }

    /**
     * 使用2字节就可以表示b
     *
     * @param b
     * @return
     */
    public static String numToHex16(int b) {
        return String.format("%04x", b);
    }

    /**
     * 使用4字节就可以表示b
     *
     * @param b
     * @return
     */
    public static String numToHex32(int b) {
        return String.format("%08x", b);
    }

    /**
     * 字节转十六进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex.toUpperCase();
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串,Hex的范围为0x00到0xFF
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * hex字符串转byte数组,如果Hex超过0xFF，显然转换后结果不是一个byte，而是一个byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexLen = inHex.length();
        byte[] result;
        if (hexLen % 2 == 1) {
            //奇数
            hexLen++;
            result = new byte[(hexLen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexLen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexLen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }


    /**
     * 数组大小分割
     *
     * @param bytes 被分割的数组
     * @param size  每个数组分割后的大小，小于等于size
     * @return
     */
    public static List<byte[]> splitBytes(byte[] bytes, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size不能小于等于0");
        }
        if (size > bytes.length) {
            throw new RuntimeException("size:" + size + ",不能大于bytes的大小:" + bytes.length);
        }
        List<byte[]> temp = new ArrayList<>();

        int packSize = bytes.length % size == 0 ? bytes.length / size : bytes.length / size + 1;

        int from = 0, to = size;
        for (int i = 0; i < packSize; i++) {
            if (to > bytes.length)
                to = bytes.length;
            byte[] bytes1 = Arrays.copyOfRange(bytes, from, to);
            from = to;
            to += size;
            temp.add(bytes1);
        }
        return temp;
    }
}