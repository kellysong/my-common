package com.sjl.core.mvp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.sjl.core.net.RxManager;
import com.sjl.core.util.TUtils;
import com.sjl.core.util.ToastUtils;

import cn.feng.skin.manager.base.BaseSkinPreferenceFragment;

/**
 * 设置fragment
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BasePreferenceFragment.java
 * @time 2018/11/26 15:47
 * @copyright(C) 2018 song
 */
public abstract class BasePreferenceFragment<T extends BasePresenter> extends BaseSkinPreferenceFragment implements BaseView {
    protected Activity mActivity;
    protected T mPresenter;
    protected RxManager rxManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxManager = new RxManager();
        addPreferencesFromResource(getPreferencesResId());


        mActivity = getActivity();
        initView();
        mPresenter = TUtils.getT(this, 0);
        if (mPresenter != null) {
            mPresenter.mContext = mActivity;
            mPresenter.attachView(this);
        }
        initListener();
        initData();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View rootView = getView();
        ListView list = (ListView) rootView.findViewById(android.R.id.list);
        list.setDivider(null); //此句去掉横线
    }

    protected T getPresenter() {
        if (mPresenter == null) throw new IllegalStateException("Presenter not created.");
        else return mPresenter;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        setClickPreferenceKey(preference,preference.getKey());//选项点击事件
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    protected abstract void setClickPreferenceKey(Preference preference, String key);

    /**
     * 获取设置资源文件id
     *
     * @return
     */
    protected abstract int getPreferencesResId();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化view点击事件
     */
    protected abstract void initListener();


    /**
     * 初始化数据，使用presenter调用数据
     */
    protected abstract void initData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        } else {
            rxManager.clear();
        }
    }

    /**
     * 通过Class跳转界面
     **/
    public void openActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 通过Class跳转界面(有回调)
     **/
    public void openActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面(有回调)
     **/
    public void openActivityForResult(Class<?> cls, Bundle bundle,
                                      int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    /**
     * 短暂显示Toast提示(id)
     **/
    public void showShortToast(int resId) {
        ToastUtils.showShort(mActivity, resId);
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    public void showShortToast(String text) {
        ToastUtils.showShort(mActivity, text);
    }


    /**
     * 长时间显示Toast提示(id)
     **/
    public void showLongToast(int resId) {
        ToastUtils.showLong(mActivity, resId);
    }

    /**
     * 长时间显示Toast提示(来自String)
     **/
    public void showLongToast(String text) {
        ToastUtils.showLong(mActivity, text);
    }
}
