package com.sjl.core.mvvm


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sjl.core.entity.EventBusDto
import com.sjl.core.mvvm.util.ViewModelUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BaseViewModelFragment
 * @time 2021/1/7 10:32
 * @copyright(C) 2021 song
 */
abstract class BaseViewModelFragment<VM : BaseViewModel> : Fragment() {

    protected lateinit var viewModel: VM
    private val factory: ViewModelProvider.Factory? = null

    private var isFirstResume = true
    private var isFirstVisible = true
    private var isFirstInvisible = true
    private var isPrepared = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVM()
        initView()
        initListener()
        initData()
        startObserve()
    }

    private fun initVM() {
        viewModel = if (isShareViewModel()) ViewModelUtils.createActivityViewModel(this, factory, 0)
        else ViewModelUtils.createViewModel(this, factory, 1)
    }

    /**
     * 必须实现的方法
     */
    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initListener()

    abstract fun initData()

    abstract fun startObserve()

    open fun isShareViewModel(): Boolean {
        return false
    }

    protected fun getViewModelFactory(): ViewModelProvider.Factory? {
        return null
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPrepare()
    }


    override fun onResume() {
        super.onResume()
        if (isFirstResume) {
            isFirstResume = false
            return
        }
        if (userVisibleHint) {
            onUserVisible()
        }
    }

    override fun onPause() {
        super.onPause()
        if (userVisibleHint) {
            onUserInvisible()
        }
    }


    /**
     * setUserVisibleHint是在onCreateView之前调用的，那么在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
     *
     * @param isVisibleToUser
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //        LogUtils.i("isVisibleToUser="+isVisibleToUser);
        if (isVisibleToUser) { //可见
            if (isFirstVisible) {
                isFirstVisible = false
                initPrepare()
            } else {
                onUserVisible()
            }
        } else { //不可见
            if (isFirstInvisible) { //第一次不可见
                isFirstInvisible = false
                onFirstUserInvisible()
            } else { //不是第一次不可见
                onUserInvisible()
            }
        }
    }

    private fun initPrepare() {
        if (isPrepared) {
            onFirstUserVisible() //第一次可见
        } else {
            isPrepared = true
        }
    }


    /*==============下面方法可选===============*/
    /**
     * 第一次可见,只执行一次
     */
    protected abstract fun onFirstUserVisible()

    /**
     * 对用户可见，每次执行 onResume()
     */
    protected abstract fun onUserVisible()

    /**
     * 第一次不可见
     */
    protected fun onFirstUserInvisible() {
    }

    /**
     * 对用户可不见  onPause()
     * this method like the fragment's lifecycle method onPause()
     */
    protected abstract fun onUserInvisible()


}