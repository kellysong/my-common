package com.sjl.core.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 算法
 * <p/>
 * 算法采用加密模式：CBC；数据块：128；填充：PKCS5Padding
 * <p/>
 * key 与向量字符串的长度为 16 位
 *
 * @author songjiali
 * @version 1.0.0
 * @filename AES.java
 * @time 2018/11/23 10:34
 * @copyright(C) 2018 song
 */
public class AES {
    /**
     * 算法名称
     */
    public static final String NAME = "AES";

    /**
     * 加密模式：CBC；数据块：128；填充：PKCS5Padding
     */
    public final String MODE = "AES/CBC/PKCS5Padding";

    /**
     * KEY 与 向量字符串的长度
     */
    public static final int LENGTH = 16;


    /**
     * 加密用的 KEY
     */
    private String key;

    /**
     * 向量，用于增加加密强度
     */
    private String ivParameter;

    /**
     * @param key         加密用的 KEY
     * @param ivParameter 偏移量
     */
    public AES(String key, String ivParameter) {
        if (key == null || key.length() != LENGTH) {
            throw new RuntimeException("KEY 不存在，或者长度不为 " + LENGTH);
        }
        if (ivParameter == null || ivParameter.length() != LENGTH) {
            throw new RuntimeException("ivParameter 不存在，或者长度不为 " + LENGTH);
        }

        this.key = key;
        this.ivParameter = ivParameter;
    }


    /**
     * 加密
     *
     * @param s 要加密的字符串
     * @return 加密后的字符串
     */
    public String encode(String s) {
        String result;
        try {

            Cipher cipher = Cipher.getInstance(MODE);
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), NAME), iv);
            byte[] bytes = cipher.doFinal(s.getBytes());
            result = Base64.encodeToString(bytes,Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException("加密", e);
        }
        return result;
    }

    /**
     * 解密
     *
     * @param s 待解密的字符串
     * @return 解密后的字符串
     */
    public String decode(String s) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("ASCII"), NAME);
            Cipher cipher = Cipher.getInstance(MODE);
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] decode = Base64.decode(s, Base64.DEFAULT);
            return new String(cipher.doFinal(decode));
        } catch (Exception e) {
            throw new RuntimeException("解密", e);
        }
    }

}
