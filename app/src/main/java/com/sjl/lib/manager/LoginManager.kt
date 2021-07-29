package com.sjl.lib.manager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sjl.core.util.activityresult.ActivityResultUtils
import com.sjl.lib.test.mvvm.activity.LoginActivity

/**
 * 登陆跳转封装，有些功能必须登陆才能跳转
 * @author Kelly
 * @version 1.0.0
 * @filename LoginManager
 * @time 2021/7/29 12:09
 * @copyright(C) 2021 song
 */
const val KEY_LOGIN_RESULT = "key_login_result"
const val RESULT_CODE_LOGIN = 101

object LoginManager {
    //通过外部修改登录标志
    var isLogin = false
    var requestCode = 100


    fun toLogin(activity: Activity, bundle: Bundle?, loginCallback: LoginCallback) {
        if (isLogin) {
            //已经登录过
            loginCallback.onLoginResult(true)
            return
        }
        val intent = Intent(activity, LoginActivity::class.java)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        ActivityResultUtils.init(activity).startActivityForResult(intent, requestCode, object : ActivityResultUtils.Callback {

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                if (requestCode == this@LoginManager.requestCode && resultCode == RESULT_CODE_LOGIN
                        && data?.getBooleanExtra(KEY_LOGIN_RESULT, false) == true) {
                    isLogin = true
                    loginCallback.onLoginResult(true)
                } else {
                    isLogin = false
                    loginCallback.onLoginResult(false)
                }
            }
        })
    }

    fun toLogin(activity: Activity, loginCallback: LoginCallback) {
        toLogin(activity, null, loginCallback)
    }

    fun toLogin(fragment: Fragment, loginCallback: LoginCallback) {
        toLogin(fragment, null, loginCallback)
    }

    fun toLogin(fragment: Fragment, bundle: Bundle?, loginCallback: LoginCallback) {
        fragment.activity?.let { toLogin(it, bundle, loginCallback) }
    }


    interface LoginCallback {
        fun onLoginResult(loginResult: Boolean)
    }

}