package com.sjl.core.util.datetime;

import com.sjl.core.util.LogUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间转换工具类add by Kelly on 20170308.
 */
public class TimeUtils {


    /**
     * 1分钟60秒
     */
    private static final int SECONDS_OF_1MINUTE = 60;

    /**
     * 1小时60*60秒
     */
    private static final int SECONDS_OF_1HOUR = 60 * SECONDS_OF_1MINUTE;

    /**
     * 1天秒数
     */
    private static final int SECONDS_OF_1DAY = 24 * SECONDS_OF_1HOUR;

    /**
     * 30天秒数
     */
    private static final int SECONDS_OF_30DAYS = SECONDS_OF_1DAY * 30;


    public static final String DATEFORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATEFORMAT_2 = "yyyy-MM-dd HH:mm";
    public static final String DATEFORMAT_3 = "HH:mm";
    public static final String DATEFORMAT_4 = "yyyy-MM-dd";
    public static final String DATEFORMAT_5 = "MM-dd HH:mm";

    public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";

    private static final int HOUR_OF_DAY = 24;
    private static final int DAY_OF_YESTERDAY = 2;
    private static final int TIME_UNIT = 60;


    private TimeUtils() {

    }


    /**
     * Date转换成字符串日期
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String formatDateToStr(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }


    /**
     * 将时间转换成日期
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String formatDateToStr(long timeInMillis, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Date date = calendar.getTime();
        return formatDateToStr(date, dateFormat);
    }

    /**
     * 字符串日期转date
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static Date strToDate(String date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算时间差
     *
     * @param starTime 开始时间
     * @param endTime  结束时间
     * @param type     返回类型 ==1----天，时，分,秒。 ==2----时，分
     * @return 返回时间差
     */
    public static String getTimeDifference(String starTime, String endTime, int type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = dateFormat.parse(starTime);
            Date endDate = dateFormat.parse(endTime);
            return computeTime(startDate, endDate, type);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 计算时间差（即距离某时间还剩多少时间）
     *
     * @param startDatetime 开始时间
     * @param endTime       结束时间
     * @param type          返回类型 ==1----天，时，分,秒。 ==2----时，分
     * @return 返回时间差
     */
    public static String getTimeDifference(Date startDatetime, String endTime, int type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date endDate = dateFormat.parse(endTime);
            return computeTime(startDatetime, endDate, type);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String computeTime(Date startDate, Date endDate, int type) {
        String timeString = "";
        long diff = (endDate.getTime() - startDate.getTime()) / 1000;//秒
        if (diff <= 0) {
            return "-1";//结束
        }
        if (type == 0) {
            long day = diff / (SECONDS_OF_1DAY);
            long hour = (diff / (SECONDS_OF_1HOUR) - day * 24);
            long min = ((diff / (SECONDS_OF_1MINUTE)) - day * 24 * 60 - hour * 60);
            long s = (diff - day * SECONDS_OF_1DAY - hour * SECONDS_OF_1HOUR - min * 60);
            timeString = day + "天" + hour + "小时" + min + "分" + s + "秒";
        } else if (type == 1) {
            long hour1 = diff / (SECONDS_OF_1HOUR);
            long min1 = ((diff / (SECONDS_OF_1MINUTE)) - hour1 * 60);
            timeString = hour1 + "小时" + min1 + "分";
        }
        return timeString;
    }


    /**
     * 获取已过时间
     *
     * @param startTime
     * @return
     */
    public static String getRangeByStringTime(String startTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT_1);
        Date date = null;
        try {
            date = sdf.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertTime(date, new Date());

    }

    /**
     * 获取已过时间
     *
     * @param startTime
     * @return
     */
    public static String getRangeByDate(Date startTime) {
        return convertTime(startTime, new Date());
    }

    /**
     * 时间差转换
     *
     * @param startTime
     * @param curDate
     * @return
     */
    private static String convertTime(Date startTime, Date curDate) {
        /**除以1000是为了转换成秒*/
        long between = (curDate.getTime() - startTime.getTime()) / 1000;
        int elapsedTime = (int) (between);
        if (elapsedTime < SECONDS_OF_1DAY) {
            return "今天";
        }
        if (elapsedTime < SECONDS_OF_30DAYS) {//30天之前

            return elapsedTime / SECONDS_OF_1DAY + "天前";
        }

        return formatDateToStr(startTime, DATEFORMAT_4);
    }


    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return "";
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff < SECONDS_OF_1MINUTE) {
            return "刚刚";
        } else if (diff >= SECONDS_OF_1MINUTE && diff < SECONDS_OF_1HOUR) {
            r = (diff / SECONDS_OF_1MINUTE);
            return r + "分钟前";
        } else if (diff >= SECONDS_OF_1HOUR && diff < SECONDS_OF_1DAY) {
            r = (diff / SECONDS_OF_1HOUR);
            return r + "小时前";
        } else if (diff >= SECONDS_OF_1DAY && diff < SECONDS_OF_1DAY * 4) {
            r = (diff / SECONDS_OF_1DAY);
            return r + "天前";//1,2,3天前
        } else {
            Calendar start = Calendar.getInstance();
            start.setTime(date);
            Calendar current = Calendar.getInstance();
            int year = current.get(Calendar.YEAR) - start.get(Calendar.YEAR);
            if (year > 0) {//超过一年
                return formatDateToStr(date, "yyyy-MM-dd");//月显示两位,MM-dd
            } else {
                return formatDateToStr(date, "MM-dd");//月显示两位,MM-dd
            }
        }

    }


    /**
     * 将日期转换成昨天、今天、明天
     *
     * @param source
     * @param pattern
     * @return
     */
    public static String dateConvert(String source, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = format.parse(source);
            long curTime = calendar.getTimeInMillis();
            calendar.setTime(date);
            //将MISC 转换成 sec
            long difSec = Math.abs((curTime - date.getTime()) / 1000);
            long difMin = difSec / 60;
            long difHour = difMin / 60;
            long difDate = difHour / 60;
            int oldHour = calendar.get(Calendar.HOUR);
            //如果没有时间
            if (oldHour == 0) {
                //比日期:昨天今天和明天
                if (difDate == 0) {
                    return "今天";
                } else if (difDate < DAY_OF_YESTERDAY) {
                    return "昨天";
                } else {
                    DateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String value = convertFormat.format(date);
                    return value;
                }
            }

            if (difSec < TIME_UNIT) {
                return difSec + "秒前";
            } else if (difMin < TIME_UNIT) {
                return difMin + "分钟前";
            } else if (difHour < HOUR_OF_DAY) {
                return difHour + "小时前";
            } else if (difDate < DAY_OF_YESTERDAY) {
                return "昨天";
            } else {
                DateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd");
                String value = convertFormat.format(date);
                return value;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 两个日期相差几天 （天数=endTime - startTime）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static long dateDiff(Date startTime, Date endTime) {
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        // 获得两个时间的毫秒时间差异
        diff = endTime.getTime()
                - startTime.getTime();
        day = diff / nd;// 计算差多少天
        long hour = diff % nd / nh;// 计算差多少小时
        long min = diff % nd % nh / nm;// 计算差多少分钟
        long sec = diff % nd % nh % nm / ns;// 计算差多少秒
        // 输出结果
        LogUtils.i("时间相差：" + day + "天" + hour + "小时" + min
                + "分钟" + sec + "秒。");
        if (day >= 1) {
            return day;
        } else {
            if (day == 0) {
                return 1;
            } else {
                return 0;
            }

        }
    }

    /**
     * Date类型转换为10位时间戳
     *
     * @param time
     * @return
     */
    public static long dateToTimestamp(Date time) {
        Timestamp ts = new Timestamp(time.getTime());
        return ts.getTime() / 1000;
    }

    /**
     * long类型转换为10位时间戳
     *
     * @param time
     * @return
     */
    public static long dateToTimestamp(long time) {
        return time / 1000;
    }

    /* 将10 or 13 位时间戳转为时间字符串
     * convert the number 1407449951 1407499055617 to date/time format timestamp
     */
    public static String timestamp2Date(String strNum) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (strNum.length() == 13) {
            String date = sdf.format(new Date(Long.parseLong(strNum)));
            return date;
        } else {
            String date = sdf.format(new Date(Long.parseLong(strNum) * 1000L));
            return date;
        }
    }


}
