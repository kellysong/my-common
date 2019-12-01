package com.sjl.core.mvp;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.annotation.NonNull;

import com.sjl.core.net.RxLifecycleUtils;
import com.uber.autodispose.AutoDisposeConverter;



/**
 * presenter主要用于网络的请求以及数据的获取
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BasePresenter.java
 * @time 2018/1/29 15:41
 * @copyright(C) 2018 song
 */
public class BasePresenter<V extends BaseContract.IBaseView> implements BaseContract.IBasePresenter<V> {
    protected V mView;
    protected Context mContext;
    protected LifecycleOwner lifecycleOwner;


    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @NonNull
    protected V getView() {
        if (mView == null) throw new IllegalStateException("view not attached.");
        else return mView;
    }


    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        if (null == lifecycleOwner)
            throw new NullPointerException("lifecycleOwner == null");
        return RxLifecycleUtils.<T>bindLifecycle(lifecycleOwner);
    }

    public void setLifecycleOwner(@NonNull LifecycleOwner owner) {
        this.lifecycleOwner = owner;
    }


    /*下面三个方法fragment用到,覆写即可*/

    public void onFirstUserVisible() {

    }

    public void onUserVisible() {

    }

    public void onUserInvisible() {

    }
}
