package cn.feng.skin.manager.base;

import android.app.Application;

import cn.feng.skin.manager.loader.SkinManager;


public class BaseSkinApplication extends Application {

    public void onCreate() {
        super.onCreate();
        initSkinLoader();
    }

    /**
     * Must call init first
     */
    private void initSkinLoader() {
        SkinManager.getInstance().init(this);
        SkinManager.getInstance().load();
    }
}
