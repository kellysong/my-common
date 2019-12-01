package cn.feng.skin.manager.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by li on 2015/8/3.
 * 这个类的作用是是设置状态栏的颜色，依赖了SystemBarTintManager类
 */
public class StatusBarBackground {



    public static void setStatusBarbackColor(Activity activity, int color)//记得在布局文件根组件上添加android:fitsSystemWindows="true"
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity,true);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            // tintManager.setStatusBarTintResource(color);
            tintManager.setStatusBarTintColor(color);
        }
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        WindowManager.LayoutParams winParams = activity.getWindow().getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        activity.getWindow().setAttributes(winParams);
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colorResId);

            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
    }
    public  void setWindowStatusBarColor2(Activity activity, int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colorResId);

            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
    }
}
