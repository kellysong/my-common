package com.sjl.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sjl.core.app.BaseApplication;
import com.sjl.core.util.log.LogUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ViewUtils.java
 * @time 2018/3/8 16:08
 * @copyright(C) 2018 song
 */
public class ViewUtils {

    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    /**
     * 获取资源
     *
     * @return
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 获取颜色id
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    /**
     * 获取字符串id
     *
     * @param id
     * @return
     */
    public static String getString(int id) {
        return getResource().getString(id);
    }

    /**
     * 运行在主线程
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (android.os.Process.myTid() == BaseApplication.getMainTid()) {
            runnable.run();// 抽象方法
        } else {
            // 主线程hanler
            BaseApplication.getHandler().post(runnable);
        }

    }

    /**
     * View延迟执行
     *
     * @param r
     * @param delayMillis
     */
    public static void postDelayed(Runnable r, long delayMillis) {
        BaseApplication.getHandler().postDelayed(r, delayMillis);
    }

    /**
     * 根据布局id获取填充的布局
     *
     * @param layoutId
     * @return
     */
    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    /**
     * 根据布局id获取自定义控件属性
     *
     * @param layoutId
     * @return
     */
    public static AttributeSet getAttributes(int layoutId) {
        AttributeSet attributes = null;
        try {
            XmlPullParser parser = getResource().getXml(layoutId);
            attributes = Xml.asAttributeSet(parser);
            LogUtils.i("节点个数：" + parser.getAttributeCount());
        } catch (Resources.NotFoundException e) {
            LogUtils.e("xml不存在", e);
        }
        return attributes;
    }

    /**
     * 移除父亲视图
     *
     * @param v
     */
    public static void removeParent(View v) {
        ViewParent parent = v.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(v);
        }
    }


    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
     */
    public static void toggleKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示密码键盘
     *
     * @param context
     */
    public static void showKeyBoard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 显示密码键盘
     *
     * @param context
     */
    public static void showKeyBoard(Context context, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏密码键盘
     *
     * @param context
     * @param editText
     */
    public static void hideKeyBoard(Context context, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 打开activity
     *
     * @param context
     * @param clz
     */
    public static void openActivity(Context context, Class<?> clz) {
        context.startActivity(new Intent(context, clz));
    }

    /**
     * 保存view为文件
     *
     * @param view
     * @param filePath
     */
    public static void saveBitmap(View view, String filePath) {
        saveBitmap(view,view.getWidth(), view.getHeight(),filePath);
    }
    /**
     * 保存view为文件
     *
     * @param view
     * @param filePath
     */
    public static void saveBitmap(View view,int width, int height, String filePath) {
        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(width,height,
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        //存储
        FileOutputStream outStream = null;
        File file = new File(filePath);
        if (file.isDirectory()) {//如果是目录不允许保存
            return;
        }
        file.delete();
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (IOException e) {
            LogUtils.e("view转图片异常", e);
        }
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        // 0.5f 四舍五入的左右 效率比 math 的四舍五入效率高
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp，保证文字大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px，保证文字大小不变
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转px
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2sp(Context context, int px) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }




    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }


    /**
     * 获取当前屏幕宽，不包括虚拟键，单位px
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取当前屏幕高，不包括虚拟键，单位px
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getHeight();
    }

    /**
     * 获取StatusBar的高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        Resources resources = BaseApplication.getContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }


    /**
     * 获取NavigationBar虚拟按键的高度
     *
     * @return
     */
    public static int getNavigationBarHeight() {
        int navigationBarHeight = 0;
        Resources rs = BaseApplication.getContext().getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && hasNavigationBar()) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }


    /**
     * 是否存在虚拟按键
     *
     * @return
     */
    private static boolean hasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = BaseApplication.getContext().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }


}
