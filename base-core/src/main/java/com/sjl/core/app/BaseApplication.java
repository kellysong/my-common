package com.sjl.core.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.sjl.core.util.log.LogUtils;
import com.sjl.core.util.log.LogWriter;
import com.sjl.core.util.log.LoggerUtils;
import com.sjl.core.util.ResourcesUtils;

import cn.feng.skin.manager.base.BaseSkinApplication;
import cn.feng.skin.manager.util.L;

/**
 * 当前应用
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BaseApplication.java
 * @time 2017年9月5日 下午5:41:39
 * @copyright(C) 2017 song
 */
public class BaseApplication extends BaseSkinApplication {

    private static BaseApplication mContext;
    private static int mainTid;
    private static Handler mHandler;
    private static String mVersion;
    private static int mVersionCode;
    private boolean LOG_DEBUG_MODE = true;//日志调试模式，true打印日志，false不打印，发布时请关闭日志
    private static final String LOG_TAG = "SIMPLE_LOGGER";

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        mContext = this;
        mainTid = android.os.Process.myTid();
        mHandler = new Handler();
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            mVersion = packageInfo.versionName;
            mVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ResourcesUtils.init(this);
    }


    /**
     * 初始化日志
     */
    protected void initLogConfig(boolean isLogEnable) {
        this.LOG_DEBUG_MODE = isLogEnable;
        L.setDebug(LOG_DEBUG_MODE);
        LoggerUtils.init(LOG_TAG, LOG_DEBUG_MODE);
        LogUtils.init(LOG_TAG, LOG_DEBUG_MODE);//初始化日志
        LogWriter.init(LOG_TAG, true, true);
    }

    /**
     * 获取主线程id h
     *
     * @return
     */
    public static int getMainTid() {
        return mainTid;
    }

    /**
     * 获取handler
     *
     * @return
     */
    public static Handler getHandler() {
        return mHandler;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static BaseApplication getContext() {
        return mContext;
    }

    /**
     * 获取应用版本名称
     *
     * @return
     */
    public static String getAppVersion() {
        return mVersion;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode() {
        return mVersionCode;
    }

}
