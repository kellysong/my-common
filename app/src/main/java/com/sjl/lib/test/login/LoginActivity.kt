package com.sjl.lib.test.login

import android.content.Intent
import android.view.View
import com.sjl.core.mvvm.BaseActivity
import com.sjl.lib.R
import com.sjl.lib.manager.KEY_LOGIN_RESULT
import com.sjl.lib.manager.RESULT_CODE_LOGIN


/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename LoginActivity
 * @time 2021/7/29 12:28
 * @copyright(C) 2021 song
 */
class LoginActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.login_layout
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initData() {

    }

    fun btnLogin(view: View) {
        val data = Intent()
        data.putExtra(KEY_LOGIN_RESULT, true)
        setResult(RESULT_CODE_LOGIN,data)
        finish()
    }
}