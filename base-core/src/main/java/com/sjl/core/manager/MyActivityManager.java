package com.sjl.core.manager;

import android.app.Activity;
import android.os.Build;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * activity
 *<pre>
 * {@code
 *
 *  registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
 *            @Override
 *            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
 *                MyActivityManager.getInstance().addActivity(activity);
 *            }
 *            @Override
 *            public void onActivityStarted(Activity activity) {
 *            }
 *            @Override
 *            public void onActivityResumed(Activity activity) {
 *                      MyActivityManager.getInstance().setCurrentActivity(activity);
 *            }
 *            @Override
 *            public void onActivityPaused(Activity activity) {
 *            }
 *            @Override
 *            public void onActivityStopped(Activity activity) {
 *            }
 *            @Override
 *            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
 *            }
 *            @Override
 *            public void onActivityDestroyed(Activity activity) {
 *                MyActivityManager.getInstance().removeActivity(activity);
 *            }
 *        });
 *        }
 *        </pre>
 * @author Kelly
 * @version 1.0.0
 * @filename MyActivityManager.java
 * @time 2019/7/3 17:02
 * @copyright(C) 2019 song
 */
public class MyActivityManager {
    private static MyActivityManager sInstance = new MyActivityManager();
    private WeakReference<Activity> sCurrentActivityWeakRef;
    private List<Activity> activityList = new LinkedList<Activity>();

    private MyActivityManager() {
    }

    public synchronized static MyActivityManager getInstance() {
        return sInstance;
    }

    /**
     * 获取栈顶Activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        getCurrentActivity();
        sCurrentActivityWeakRef = new WeakReference<>(activity);
    }

    /**
     * MyActivityManager
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (!activityList.contains(activity))
            activityList.add(activity);
    }

    /**
     * remove Activity
     * @param activity
     */
    public void removeActivity(Activity activity) {
        getCurrentActivity();
        if (activityList.contains(activity))
            activityList.remove(activity);
    }

    /**
     * 根据class获取实例
     *
     * @param clz
     * @return
     */
    public Activity getActivity(Class<?> clz) {
        int size = activityList.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activityList.get(i);
            if (activity.getClass() == clz) {
                return activity;
            }
        }
        return null;
    }


    /**
     * 根据class关闭指定Activity
     *
     * @param clz
     * @return
     */
    public void finishActivity(Class<?> clz) {
        Activity activity = getActivity(clz);
        if (activity != null){
            activityList.remove(activity);
            activity.finish();
        }

    }


    /**
     * 关闭所有activity
     */
    public void finishActivityList() {
        Iterator<Activity> iterator = activityList.iterator();
        while (iterator.hasNext()){
            Activity activity = iterator.next();
            activity.finish();
            iterator.remove();
        }
    }

    /**
     * 获取栈顶Activity
     *
     * @return
     */
    public Activity getTopActivity() {
        Activity activity = null;
        int size = activityList.size();
        if (size > 0) {
            activity = activityList.get(size -1);;
        }
        return activity;
    }

    /**
     * 判断Activity class是否处于栈顶
     *
     * @param clz
     * @return
     */
    protected boolean isTopActivity(Class<?> clz) {
        Activity topActivity = getTopActivity();
        if (topActivity != null && topActivity.getClass() == clz) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个Activity class 是否存在
     *
     * @param clz
     * @return
     */
    public <T extends Activity> boolean isActivityExist(Class<T> clz) {
        boolean res = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Activity activity = getActivity(clz);
            if (activity == null) {
                res = false;
            } else {
                if (activity.isFinishing() || activity.isDestroyed()) {
                    res = false;
                } else {
                    res = true;
                }
            }
        }
        return res;
    }


    /**
     * 判断Activity是否Destroy
     * @param mActivity
     * @return true:已销毁
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null ||
                mActivity.isFinishing() ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

    public List<Activity> getActivityList() {
        return activityList;
    }
}
