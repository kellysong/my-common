package com.sjl.lib.test

import com.sjl.core.mvvm.BaseActivity
import com.sjl.lib.R
import com.sjl.lib.test.mvc.NetActivity
import com.sjl.lib.test.mvi.activity.NetActivity4
import com.sjl.lib.test.mvp.activity.NetActivity2
import com.sjl.lib.test.mvvm.activity.NetActivity3

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename SoftwareArchActivity
 * @time 2022/5/20 9:54
 * @copyright(C) 2022 song
 */
class SoftwareArchActivity  : BaseActivity(){
    override fun getLayoutId(): Int {
        return R.layout.software_arch_activity
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initData() {

    }
    fun btnMvc(view: android.view.View) {
        openActivity<NetActivity>(this)
    }
    fun btnMvp(view: android.view.View) {
        openActivity<NetActivity2>(this)
    }
    fun btnMvvm(view: android.view.View) {
        openActivity<NetActivity3>(this)
    }
    fun btnMvi(view: android.view.View) {
        openActivity<NetActivity4>(this)
    }

}