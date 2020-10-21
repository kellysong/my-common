package com.sjl.core.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.sjl.core.util.log.LogUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * 
 * app工具类
 * 
 * @author Kelly
 * @version 1.0.0
 * @filename PushUtils.java
 * @time 2017年8月25日 下午2:17:10 
 * @copyright(C) 2017 song
 */
public class AppUtils {

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * 0代表连接的是WiFi，1代表连接的是GPRS,2 代表无网络
	 * 
	 * @return
	 */
	public static int getNetworkAvailableType(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return 2;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return 2;
		} else {
			boolean wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
			if (wifi) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	/**
	 * 判断应用是否在运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAppRunning(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		boolean isAppRunning = false;
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(context.getPackageName())
					|| info.baseActivity.getPackageName().equals(context.getPackageName())) {
				isAppRunning = true;
				LogUtils.i(info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="
						+ info.baseActivity.getPackageName());
				break;
			}
		}
		return isAppRunning;
	}

	/**
	 * 判断服务是否运行
	 * @param context 上下文
	 * @param className 服务类名
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String className) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(100);
		if (info == null || info.size() == 0) return false;
		for (ActivityManager.RunningServiceInfo aInfo : info) {
			if (className.equals(aInfo.service.getClassName())) return true;
		}
		return false;
	}



	/**
	 * 重启app
	 * @param context
	 */
	public static void restartApp(Context context) {
		PackageManager packageManager = context.getPackageManager();
		if (null == packageManager) {
			LogUtils.e("null == packageManager");
			return;
		}
		final Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
		if (intent != null) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(intent);
		}
	}



	/**
	 * 验证对象属性是否为空
	 * 
	 * @param obj
	 *            待验证对象
	 * @param excludeField
	 *            要排除验证的对象属性
	 * @return
	 * @throws IllegalAccessException
	 */
	public static boolean checkObjFieldIsNull(Object obj, String[] excludeField) throws IllegalAccessException {
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field f : declaredFields) {
			f.setAccessible(true);
			String name = f.getName();
			for (int i = 0; i < excludeField.length; i++) {
				if (!name.equals(excludeField[i])) {
					Object object = f.get(obj);
					if (object == null || (object != null && "".equals(object.toString().trim()))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 字符串空判断
	 * 
	 * @param str
	 *            true为空
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str)) {
			return true;
		}else if(str.trim().length() == 0){
			return true;
		}else if(str.equals("null")) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串非空判断
	 *
	 * @param str
	 *            true为空
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 集合空判断
	 *
	 * @param str
	 *            true为空
	 * @return
	 */
	public static boolean isEmpty(Collection str) {
		return str == null || str.isEmpty() ? true : false;
	}

	/**
	 * 集合非空判断
	 *
	 * @param str
	 *            true为空
	 * @return
	 */
	public static boolean isNotEmpty(Collection str) {
		return !isEmpty(str);
	}

	/**
	 * 比较当前url知否在指定页面中
	 * 
	 * @param currentUrl
	 *            当前页面
	 * @param comparePages
	 *            需要比较的页面数组
	 * @return
	 */
	public static boolean compareHtmlPage(String currentUrl, String[] comparePages) {
		if (comparePages == null) {
			return false;
		}
		for (String pageUrl : comparePages) {
			if (currentUrl.contains(pageUrl)) {
				return true;
			}
		}
		return false;
	}



	/**
	 * 获取url 指定name的value
	 * @param url 请求路径
	 * @param name 名字
	 * @return
	 */
	public static String getUrlValueByName(String url, String name) {
		String result = "";
		int index = url.indexOf("?");
		String temp = url.substring(index + 1);
		String[] keyValue = temp.split("&");
		for (String str : keyValue) {
			if (str.contains(name)) {
				result = str.replace(name + "=", "");
				break;
			}
		}
		return result;
	}

}
