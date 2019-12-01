package com.sjl.core.net;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;


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


    private static LifecycleOwner lifecycleOwner;

    /**
     * 适用于fragment没有LifecycleOwner的情况，比如service、其它类使用rxjava等情况
     * <p>在宿主activity onResume中手动注入</p>
     *
     * @param lifecycleOwner
     */
    public static void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        RxLifecycleUtils.lifecycleOwner = lifecycleOwner;
    }

    public static <T> AutoDisposeConverter<T> bindLifecycle() {
        if (null == lifecycleOwner)
            throw new NullPointerException("lifecycleOwner is null.");
        return AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner,Lifecycle.Event.ON_DESTROY));
    }


}
