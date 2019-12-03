package com.sjl.core.net;

import com.sjl.core.util.log.LogUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * RxJava订阅管理,处理autoDispose不支持的情况（如：PreferenceFragment、service、其他类），还没有找到解决方法
 *
 * @author songjiali
 * @version 1.0.0
 * @filename RxManager.java
 * @time 2018/10/18 14:11
 * @copyright(C) 2018 song
 */
public class RxManager {


    //请求队列
    private CompositeDisposable compositeDisposable;


    public RxManager() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
    }

    public void add(Disposable d) {
        compositeDisposable.add(d);
    }

    /**
     * 将所有的 observer 取消订阅
     */
    public void clear() {
        int size = compositeDisposable.size();
        if (size > 0) {
            LogUtils.i("clear:" + size);
            compositeDisposable.clear();//clear()可以多次被调用来丢弃容器中所有的Disposable，但dispose()被调用一次后就会失效,导致重新进来activity再订阅rxjava无法回调观察者
        }
    }

    public void dispose() {
        int size = compositeDisposable.size();
        if (size > 0) {
            LogUtils.i("dispose:" + size);
            compositeDisposable.dispose();
        }
    }
}
