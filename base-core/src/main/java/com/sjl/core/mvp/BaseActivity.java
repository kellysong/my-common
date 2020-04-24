package com.sjl.core.mvp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.sjl.core.R;
import com.sjl.core.net.RxLifecycleUtils;
import com.sjl.core.net.RxManager;
import com.sjl.core.util.TUtils;
import com.sjl.core.util.ToastUtils;
import com.sjl.core.util.log.LogUtils;
import com.sjl.core.widget.loadingdialog.LoadingDialog;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.feng.skin.manager.base.BaseSkinActivity;
import io.reactivex.disposables.Disposable;

/**
 * MVP基类activity
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BaseSkinActivity.java
 * @time 2018/3/2 15:29
 * @copyright(C) 2018 song
 */
public abstract class BaseActivity<T extends BasePresenter> extends BaseSkinActivity implements BaseView {
    private final static List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
    protected T mPresenter;
    protected Context mContext;
    protected Unbinder unbind;

    private LoadingDialog loadingDialog;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        synchronized (mActivities) {
            mActivities.add(this);
        }
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
        }
        mContext = this;
        unbind = ButterKnife.bind(this);
        mPresenter = TUtils.getT(this, 0);
        if (mPresenter != null) {
            mPresenter.mContext = this;
            mPresenter.setLifecycleOwner(this);
            mPresenter.attachView(this);
        }
        initTab();
        initView();
        initListener();
        initData();
    }


    protected void initTab() {

    }

    protected <R> AutoDisposeConverter<R> bindLifecycle() {

        return RxLifecycleUtils.<R>bindLifecycle(this);
    }


    @NonNull
    protected T getPresenter() {
        if (mPresenter == null) throw new IllegalStateException("Presenter not created.");
        else return mPresenter;
    }

    /**
     * 用于autoDispose不支持的情况
     */
    private RxManager rxManager;

    protected void addDisposable(Disposable disposable) {
        if (rxManager == null) {
            rxManager = new RxManager();
        }
        rxManager.add(disposable);
    }


    /**
     * 获取布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

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


    /**
     * 绑定Toolbar并设置标题
     *
     * @param toolbar
     * @param title   标题
     */
    protected void bindingToolbar(Toolbar toolbar, String title) {
        if (toolbar == null) {
            LogUtils.w("toolbar未实例化，返回失败");
            return;
        }
        if (!TextUtils.isEmpty(title)) {
            toolbar.setTitle(title);
        }
        setSupportActionBar(toolbar);//加了这个才会触发toolbar菜单创建onCreateOptionsMenu
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 设置StatusBar颜色
     *
     * @param color 颜色值
     */
    protected void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }


    /**
     * 通过Class跳转界面
     **/
    public void openActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
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
        intent.setClass(this, cls);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面(有回调)
     **/
    public void openActivityForResult(Class<?> cls, Bundle bundle,
                                      int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    /**
     * 显示Toast，不会重叠，会覆盖前面的
     *
     * @param msg
     */
    protected void showToast(String msg) {
        if (mToast != null) {
            mToast.setText(msg);
        } else {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


    /**
     * 短暂显示Toast提示(id)
     **/
    public void showShortToast(int resId) {
        ToastUtils.showShort(this, resId);
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    public void showShortToast(String text) {
        ToastUtils.showShort(this, text);
    }


    /**
     * 长时间显示Toast提示(id)
     **/
    public void showLongToast(int resId) {
        ToastUtils.showLong(this, resId);
    }

    /**
     * 长时间显示Toast提示(来自String)
     **/
    public void showLongToast(String text) {
        ToastUtils.showLong(this, text);
    }


    /**
     * 显示加载框
     */
    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, getString(R.string.text_loading_dialog));
        }
        if (!isFinishing()) {
            loadingDialog.show();
        }
    }

    /**
     * 显示加载框
     *
     * @param msg 加载信息
     */
    public void showLoadingDialog(String msg) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, getString(R.string.text_loading_dialog));
        }
        if (!isFinishing()) {
            loadingDialog.setLoadingMsg(msg);
            loadingDialog.show();
        }

    }

    /**
     * 隐藏加载框
     */
    public void hideLoadingDialog() {
        if (loadingDialog == null) {
            return;
        }
        loadingDialog.close();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (mActivities) {
            mActivities.remove(this);
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (rxManager != null) {
            rxManager.dispose();
        }
        if (unbind != null) {
            unbind.unbind();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);//权限申请
    }

    /**
     * 销毁activity
     */

    public void killAll() {
        synchronized (mActivities) {
            Iterator<BaseActivity> it = mActivities.iterator();
            while (it.hasNext()) {
                BaseActivity next = it.next();
                next.finish();
            }
        }
    }

    /**
     * 销毁Activity，但不包括clz
     *
     * @param clz
     */
    protected void killAllExcludeOneself(Class<?> clz) {
        synchronized (mActivities) {
            Iterator<BaseActivity> it = mActivities.iterator();
            while (it.hasNext()) {
                BaseActivity next = it.next();
                if (next.getClass() == clz) {
                    continue;
                }
                next.finish();
            }
        }
    }

    /**
     * 销毁单个Activity
     *
     * @param clz
     */
    protected void killSingleActivity(Class<?> clz) {
        synchronized (mActivities) {
            Iterator<BaseActivity> it = mActivities.iterator();
            while (it.hasNext()) {
                BaseActivity next = it.next();
                if (next.getClass() == clz) {
                    next.finish();
                    break;
                }
            }
        }
    }


    /**
     * 根据class获取实例
     *
     * @param clz
     * @return
     */
    protected BaseActivity getActivity(Class<?> clz) {
        int size = mActivities.size();
        for (int i = 0; i < size; i++) {
            BaseActivity baseActivity = mActivities.get(i);
            if (baseActivity.getClass() == clz) {
                return baseActivity;
            }
        }
        return null;
    }




    /**
     * 根据class获取实例
     *
     * @param clz
     * @return
     */
    protected boolean isTopActivity(Class<?> clz) {
        BaseActivity baseActivity = mActivities.get(mActivities.size() - 1);
        if (baseActivity != null && baseActivity.getClass() == clz) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个Activity 是否存在
     *
     * @param clz
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public <T extends Activity> boolean isActivityExist(Class<T> clz) {
        boolean res = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            BaseActivity activity = getActivity(clz);
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

    public static List<BaseActivity> getActivities() {
        return mActivities;
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
}
