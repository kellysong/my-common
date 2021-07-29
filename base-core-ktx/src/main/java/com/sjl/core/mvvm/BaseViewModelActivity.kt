package com.sjl.core.mvvm

import android.os.Bundle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.sjl.core.mvvm.util.ViewModelUtils
import com.sjl.core.mvvm.util.openActivity


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

    private val factory: ViewModelProvider.Factory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun ready() {
        //初始化viewModel
        initVM()
        startObserve()
    }

    private fun initVM() {
        viewModel = ViewModelUtils.createViewModel(this,getViewModelFactory(),0)
    }

    open fun providerVMClass(): Class<VM>? = null

    open abstract fun startObserve()


    open fun getViewModelFactory(): ViewModelProvider.Factory?{
        return null
    }
}