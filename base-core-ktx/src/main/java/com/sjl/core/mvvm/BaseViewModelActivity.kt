package com.sjl.core.mvvm

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider


/**
 *
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BaseViewModelActivity
 * @time 2021/1/7 10:32
 * @copyright(C) 2021 song
 */
abstract class BaseViewModelActivity<VM : BaseViewModel> : BaseActivity() {

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserve()
    }

    override fun ready() {
        //初始化viewModel
        initVM()
    }

    private fun initVM() {
        providerVMClass()?.let {
//            viewModel = ViewModelProviders.of(this).get(it)
            viewModel = ViewModelProvider(this).get(it)
            lifecycle.addObserver(viewModel)
        }
    }

    open fun providerVMClass(): Class<VM>? = null

    private fun startObserve() {

    }


    override fun onDestroy() {
        super.onDestroy()
        if (::viewModel.isInitialized) {
            lifecycle.removeObserver(viewModel)
        }

    }
}