package com.sjl.core.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sjl.core.R;
import com.sjl.core.entity.EventBusDto;
import com.sjl.core.net.RxLifecycleUtils;
import com.sjl.core.util.ResourcesUtils;
import com.sjl.core.util.TUtils;
import com.sjl.core.util.ToastUtils;
import com.sjl.core.widget.loadingdialog.LoadingDialog;
import com.uber.autodispose.AutoDisposeConverter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.feng.skin.manager.base.BaseSkinFragment;

/**
 * lazyLoadFragment
 * 正常流程：
 * setUserVisibleHint()->onAttach()->onCreate()->onCreateView()->onActivityCreated()->onStart()->onResume()；
 * onPause()->onStop()->onDestroyView()->onDestroy()->onDetach()
 */
public abstract class BaseFragment<T extends BasePresenter> extends BaseSkinFragment implements BaseView {

    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;
    protected FragmentActivity mActivity;
    protected T mPresenter;
    protected Unbinder unbinder;
    private LoadingDialog loadingDialog;
    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    /**
     * onCreateView是创建的时候调用，onViewCreated是在onCreateView后被触发的事件
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        mPresenter = TUtils.getT(this, 0);
        if (mPresenter != null) {
            mPresenter.mContext = mActivity;
            mPresenter.setLifecycleOwner(this);
            mPresenter.attachView(this);
        }
        initListener();
        initData();
    }

    /**
     * 初始化皮肤，不满足直接覆写重新定义
     */
    protected void initSkin() {
        Toolbar toolbar = ButterKnife.findById(mActivity, ResourcesUtils.getViewId("common_toolbar"));
        if (toolbar != null) {
            dynamicAddView(toolbar, "background", ResourcesUtils.getColorId("colorPrimary"));
        }
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {

        return RxLifecycleUtils.<T>bindLifecycle(this);
    }


    @NonNull
    protected T getPresenter() {
        if (mPresenter == null) throw new IllegalStateException("Presenter not created.");
        else return mPresenter;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    /**
     * <a href="https://blog.csdn.net/wblyuyang/article/details/51851104">Android中ViewPager + Fragment使用ButterKnife注解时出现空指针NullPoint的情况</a>
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        //这里不要解绑，真恶心，卧槽，解绑之后会空指针
//        ButterKnife.unbind(this);
//        unbinder.unbind();//未验证
        if (mPresenter != null) {
            mPresenter.detachView();
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        // for bug ---> java.lang.IllegalStateException: Activity has been destroyed
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * setUserVisibleHint是在onCreateView之前调用的，那么在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        LogUtils.i("isVisibleToUser="+isVisibleToUser);
        if (isVisibleToUser) {//可见
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {//不可见
            if (isFirstInvisible) {//第一次不可见
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {//不是第一次不可见
                onUserInvisible();
            }
        }
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();//第一次可见
        } else {
            isPrepared = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusDto event) {
        if (event != null) {
            onEventComing(event);
        }
    }


    /**
     * 获取fragment布局id
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

    /*==============下面方法可选===============*/

    /**
     * 第一次可见,只执行一次
     * when fragment is visible for the first time, here we can do some initialized work or refresh data only once
     */
    protected abstract void onFirstUserVisible();

    /**
     * 对用户可见，每次执行 onResume()
     * this method like the fragment's lifecycle method onResume()
     */
    protected abstract void onUserVisible();

    /**
     * 第一次不可见
     * when fragment is invisible for the first time
     */
    private void onFirstUserInvisible() {
        // here we do not recommend do something
    }

    /**
     * 对用户可不见  onPause()
     * this method like the fragment's lifecycle method onPause()
     */
    protected abstract void onUserInvisible();

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
     * 显示Toast，不会重叠，会覆盖前面的
     *
     * @param msg
     */
    protected void showToast(String msg) {
        if (mToast != null) {
            mToast.setText(msg);
        } else {
            mToast = Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
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


    /**
     * 显示加载框
     */
    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mActivity, getString(R.string.text_loading_dialog));
        }
        if (!mActivity.isFinishing()){
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
            loadingDialog = new LoadingDialog(mActivity, getString(R.string.text_loading_dialog));
        }
        if (!mActivity.isFinishing()){
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



    /**
     * 子类需要覆写即可
     *
     * @param eventCenter
     */
    protected void onEventComing(EventBusDto eventCenter) {

    }

}
