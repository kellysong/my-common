package com.sjl.core.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ValidatorUtils.java
 * @time 2018/2/23 17:46
 * @copyright(C) 2018 song
 */
public class ValidatorUtils {
    /**
     * 正则表达式：验证手机号
     */
    private static final String REGEX_PHONE_NUMBER = "^(0(10|2\\d|[3-9]\\d\\d)[- ]{0,3}\\d{7,8}|0?1[3584]\\d{9})$";
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_PHONE_NUMBER, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验URL
     */
    public static boolean checkUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        String temp = url.replace(" ", "");
        if (!temp.startsWith("http") && !temp.startsWith("https")) {
            temp = "https://" + temp;
        }
        boolean flag;
        try {
            String check = "(http|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
            Pattern regex = Pattern.compile(check, Pattern.CASE_INSENSITIVE);
            Matcher matcher = regex.matcher(temp.replaceAll(" ", ""));
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 校验ip地址
     *
     * @param ip
     * @return
     */
    public static boolean checkAddress(String ip) {
        return ip.matches("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))");
    }

    /**
     * 校验端口号
     *
     * @param port
     * @return
     */
    public static boolean checkPort(String port) {
        return port.matches("^[1-9]$|(^[1-9][0-9]$)|(^[1-9][0-9][0-9]$)|(^[1-9][0-9][0-9][0-9]$)|(^[1-6][0-5][0-5][0-3][0-5]$)");
    }

    /**
     *判断一个字符串的首字符是否为字母
     * @param s
     * @return
     */
    public static boolean checkLetter(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(String.valueOf(c));
        return matcher.matches();
    }

    /**
     * 判断是否是英文
     *
     * @param c
     * @return
     */
    public static boolean isEnglish(char c) {
        return String.valueOf(c).matches("^[a-zA-Z]*");
    }
}
