package com.sjl.core.net;


import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.lang.ref.WeakReference;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;


/**
 * rxjava生命周期管理
 *
 * @author Kelly
 * @version 1.0.0
 * @filename RxLifecycleUtils.java
 * @time 2018/11/28 9:22
 * @copyright(C) 2018 song
 */
public class RxLifecycleUtils {

    private RxLifecycleUtils() {
        throw new IllegalStateException("Can't instance the RxLifecycleUtils");
    }


    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner) {
        return AutoDispose.<T>autoDisposable(
                AndroidLifecycleScopeProvider.from(lifecycleOwner,Lifecycle.Event.ON_DESTROY)
        );
    }


    private static WeakReference<LifecycleOwner> weakReference;

    /**
     * 适用于fragment没有LifecycleOwner的情况，比如service、其它类使用rxjava等情况
     * <p>在宿主activity onResume中手动注入</p>
     *
     * @param lifecycleOwner
     */
    public static void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        weakReference = new WeakReference<>(lifecycleOwner);

    }

    public static <T> AutoDisposeConverter<T> bindLifecycle() {
        if (null == weakReference.get())
            throw new NullPointerException("lifecycleOwner is null.");
        return AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(weakReference.get(),Lifecycle.Event.ON_DESTROY));
    }

    /**
     * 清除
     */
    public static void clear() {
        if (weakReference == null){
            return;
        }
        weakReference.clear();
    }

}
