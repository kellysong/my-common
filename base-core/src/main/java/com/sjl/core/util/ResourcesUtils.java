package com.sjl.core.util;


import android.content.Context;
import android.content.res.Resources;

import com.sjl.core.util.log.LogUtils;

/**
 * 
 * Android资源id获取工具类，通过反射获取所在项目包下的资源文件id
 * 
 * @author songjiali
 * @version 1.0.0
 * @filename ResourcesUtils.java
 * @time 2018/10/22 17:25
 * @copyright(C) 2018 song
 */
public class ResourcesUtils {
	private static String mPackageName;// 包名
	private static Resources mResources;// 获取资源的实例

	/**
	 * 资源工具类初始化
	 * 
	 * @param context
	 *            上下文
	 */
	public static void init(Context context) {
		mPackageName = context.getApplicationContext().getPackageName();
		mResources = context.getApplicationContext().getResources();
	}

	/**
	 * 获取控件id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getViewId(String resName) {
		return mResources.getIdentifier(resName, "id", mPackageName);
	}

	/**
	 * 获取布局id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getLayoutId(String resName) {
		return mResources.getIdentifier(resName, "layout", mPackageName);
	}

	/**
	 * 获取字符串id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getStringId(String resName) {
		return mResources.getIdentifier(resName, "string", mPackageName);
	}

	/**
	 * 获取图片资源id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getDrawable(String resName) {
		return mResources.getIdentifier(resName, "drawable", mPackageName);
	}

	/**
	 * 获取图片资源id（As工程资源目录）
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getMipmapId(String resName) {
		return mResources.getIdentifier(resName, "mipmap", mPackageName);
	}

	/**
	 * 获取颜色id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getColorId(String resName) {
		return mResources.getIdentifier(resName, "color", mPackageName);
	}

	/**
	 * 获取样式id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getStyleId(String resName) {
		return mResources.getIdentifier(resName, "style", mPackageName);
	}

	/**
	 * 获取尺寸id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getDimenId(String resName) {
		return mResources.getIdentifier(resName, "dimen", mPackageName);
	}

	/**
	 * 获取数组资源id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getArrayId(String resName) {
		return mResources.getIdentifier(resName, "array", mPackageName);
	}

	/**
	 * 获取int类型资源id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getIntegerId(String resName) {
		return mResources.getIdentifier(resName, "integer", mPackageName);
	}

	/**
	 * 获取bool类型资源id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getBoolId(String resName) {
		return mResources.getIdentifier(resName, "bool", mPackageName);
	}

	/**
	 * 获取动画资源id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getAnimId(String resName) {
		return mResources.getIdentifier(resName, "anim", mPackageName);
	}

	/**
	 * 获取raw资源id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getRawId(String resName) {
		return mResources.getIdentifier(resName, "raw", mPackageName);
	}

	/**
	 * 获取属性资源id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getAttrId(String resName) {
		return mResources.getIdentifier(resName, "attr", mPackageName);
	}

	/**
	 * 获取xml资源 Id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getXmlId(String resName) {
		return mResources.getIdentifier(resName, "xml", mPackageName);
	}

	/**
	 * 获取styleable Id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int getStyleableId(String resName) {
		return mResources.getIdentifier(resName, "styleable", mPackageName);
	}

	/**
	 * 获取styleable数组资源id
	 * 
	 * @param resName
	 *            资源名称
	 * @return
	 */
	public static int[] getStyleableArrayId(String resName) {
		return getResourceIdsByName(resName, "styleable", mPackageName);
	}

	/**
	 * 利用反射，获取int数组格式的资源ID，例如styleable
	 * 
	 * @param resName
	 *            资源名称
	 * @param resType
	 *            资源类型
	 * @param packageName
	 *            包名
	 */
	private static int[] getResourceIdsByName(String resName, String resType, String packageName) {
		Class clsR = null;
		int[] ids = null;
		try {
			clsR = Class.forName(packageName + ".R");
			Class[] classes = clsR.getClasses();
			Class desClass = null;
			for (int i = 0; i < classes.length; i++) {
				String[] temp = classes[i].getName().split("\\$");
				if (temp.length >= 2) {
					if (temp[1].equals(resType)) {
						desClass = classes[i];
						break;
					}
				}
			}
			if (desClass != null) {
				ids = (int[]) desClass.getField(resName).get(resName);
			}
		} catch (Exception e) {
			LogUtils.e("",e);
		}
		return ids;
	}

	/**
	 * 通过反射获取资源id,可以获取系统R文件资源id
	 * 
	 * @param resName
	 *            资源名称
	 * @param resType
	 *            资源类型
	 * @param packageName
	 *            包名
	 * @return
	 */
	public static int getResourceIdByName(String resName, String resType, String packageName) {
		Class r = null;
		int id = 0;
		try {
			r = Class.forName(packageName + ".R");
			Class[] classes = r.getClasses();
			Class desireClass = null;
			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(resType)) {
					desireClass = classes[i];
					break;
				}
			}
			if (desireClass != null)
				id = desireClass.getField(resName).getInt(desireClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
}
