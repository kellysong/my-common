package com.sjl.core.mvp;

import android.content.Context;

import com.sjl.core.net.RxLifecycleUtils;
import com.uber.autodispose.AutoDisposeConverter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;


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
        init();
    }

    /**
     * 用于初始化参数
     */
    public void init() {

    }


    @Override
    public void detachView() {
        this.mView = null;
    }

    /**
     * 建议使用 {@link #sendToView(ViewAction)}
     * @return
     */
    public V getView() {
        return mView;
    }

    public V getViewOrThrow() {
        final V view = getView();
        if (view == null) throw new IllegalStateException("view not attached.");
        return view;
    }

    /**
     * 处理V的回调
     * @param action
     */
    public void sendToView(final ViewAction<V> action) {
        final V view = getView();
        if (view != null) {
            action.call(view);
        }
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
