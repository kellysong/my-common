package cn.feng.skin.manager.base;

import android.app.Application;
import android.os.Build;

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
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            SkinManager.getInstance().loadSync();//9.0同步换肤
        } else {
            SkinManager.getInstance().load();
        }
    }
}
