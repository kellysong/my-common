package com.sjl.core.mvvm


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

/**
 *
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BaseViewModelFragment
 * @time 2021/1/7 10:32
 * @copyright(C) 2021 song
 */
abstract class BaseViewModelFragment<VM : BaseViewModel> : Fragment(){

    private val fragmentName = javaClass.simpleName
    protected lateinit var viewModel:VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initVM()
        initView()
        initListener()
        initData()
        startObserve()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun initVM() {
        providerVMClass()?.let {
//            viewModel = ViewModelProviders.of(this).get(it)
            viewModel =  ViewModelProvider(this).get(it)
            lifecycle.addObserver(viewModel)
        }
    }

    open fun providerVMClass(): Class<VM>? = null
    open fun startObserve() {}

    /**
     * 必须实现的方法
     */
    abstract fun getLayoutId():Int

    abstract fun initView()

    abstract fun initListener()

    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        if (this::viewModel.isInitialized)
        lifecycle.removeObserver(viewModel)
    }

}