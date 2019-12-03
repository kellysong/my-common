package com.sjl.core.util.security;

import android.util.Base64;

import com.sjl.core.util.log.LogUtils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**

 * DES加解密 alter by kelly on 20170205

 * <p>法算法名称/加密模式/填充方式</p>

 * <p>加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB</p>

 * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>

 */
public class DESUtils {
	private final static String HEX = "0123456789ABCDEF";
	// DES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
	private final static String TRANSFORMATION = "DES/CBC/PKCS5Padding";
	// 初始化向量参数，AES 为16bytes. DES 为8bytes.
	private final static String IVPARAMETERSPEC = "01020304";
	// DES是加密方式
	private final static String ALGORITHM = "DES";

	// 对密钥进行处理
    private static Key getRawKey(String key) {
		DESKeySpec dks;
		try {
			dks = new DESKeySpec(key.getBytes());

			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(ALGORITHM);
			return keyFactory.generateSecret(dks);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * DES加密后转为Base64编码
	 * @param secretKey 8字节密钥
	 * @param plainText 明文
	 * @return
	 */
    public static String encodeDESToBase64(String secretKey, String plainText) {
		return Base64.encodeToString(encodeDES(secretKey,plainText.getBytes()), Base64.DEFAULT);
    }




    /**
     * DES加密
     * @param secretKey 8字节密钥
     * @param data  明文
     * @return
     */
    public static  byte[] encodeDES(String secretKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(IVPARAMETERSPEC.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, getRawKey(secretKey), iv);
            byte[] bytes = cipher.doFinal(data);
            return  bytes;
        } catch (Exception e) {
            LogUtils.e("DES加密异常",e);
            return null;
		}
	}


    /**
     * DES解密Base64编码密文
     * @param secretKey 8字节密钥
     * @param encryptText 密文
     * @return
     */
    public static String decryptBase64DES(String secretKey, String encryptText) {
        byte[] decodeDES = decodeDES(secretKey, Base64.decode(encryptText, Base64.DEFAULT));
        return new String(decodeDES);
    }



    /**
     * DES解密
     * @param secretKey 8字节密钥
     * @param data 密文
     * @return
     */
    public static byte[] decodeDES(String secretKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(IVPARAMETERSPEC.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, getRawKey(secretKey), iv);
            byte[] original = cipher.doFinal(data);
            return original;
        } catch (Exception e) {
            LogUtils.e("DES解密异常",e);
            return null;
        }
    }
}
