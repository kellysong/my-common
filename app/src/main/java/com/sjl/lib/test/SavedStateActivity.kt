package com.sjl.lib.test


import com.sjl.core.mvvm.BaseViewModelActivity
import com.sjl.core.util.log.LogUtils
import com.sjl.lib.R
import kotlinx.android.synthetic.main.saved_state_activity.*

/**
 * 模拟 Activity 由于系统内存不足被销毁的情况
 *
 * Activity 意外销毁的情况可以分为两种：

由于屏幕旋转等配置更改的原因导致 Activity 被销毁
由于系统资源限制导致 Activity 被销毁
 * @author Kelly
 * @version 1.0.0
 * @filename SavedStateActivity
 * @time 2021/4/17 15:26
 * @copyright(C) 2021 song
 */
class SavedStateActivity : BaseViewModelActivity<SavedStateViewModel>() {

    override fun providerVMClass(): Class<SavedStateViewModel>? = SavedStateViewModel::class.java

    override fun getLayoutId(): Int {
        return R.layout.saved_state_activity
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initData() {

        LogUtils.i("savedStateViewModel: $viewModel")
        LogUtils.i("savedStateViewModel.name: ${viewModel.nameLiveData.value}")
        LogUtils.i("savedStateViewModel.blog: ${viewModel.blogLiveData.value}")
        LogUtils.i("onCreate")
        btn_test.setOnClickListener {
            viewModel.nameLiveData.value = "Kelly"
            viewModel.blogLiveData.value = "https://blog.csdn.net/u011082160"
        }
    }




}