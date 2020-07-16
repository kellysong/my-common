package com.sjl.core.manager;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * activity管理
 * registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
 *
 *            @Override
 *            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
 *                MyActivityManager.getInstance().addActivity(activity);
 *            }
 *
 *            @Override
 *            public void onActivityStarted(Activity activity) {
 *
 *            }
 *
 *            @Override
 *            public void onActivityResumed(Activity activity) {
 *                      MyActivityManager.getInstance().setCurrentActivity(activity);
 *            }
 *
 *            @Override
 *            public void onActivityPaused(Activity activity) {
 *
 *            }
 *
 *            @Override
 *            public void onActivityStopped(Activity activity) {
 *
 *            }
 *
 *            @Override
 *            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
 *
 *            }
 *
 *            @Override
 *            public void onActivityDestroyed(Activity activity) {
 *                MyActivityManager.getInstance().removeActivity(activity);
 *            }
 *        });
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
     * 根据class关闭知道Activity
     *
     * @param clz
     * @return
     */
    public void finishActivity(Class<?> clz) {
        Activity activity = getActivity(clz);
        if (activity != null){
            activity.finish();
        }
    }


    /**
     * 关闭每一个list内的activity
     */
    public void finishActivityList() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public List<Activity> getActivityList() {
        return activityList;
    }
}
