package com.sjl.lib.app

import android.content.Context
import androidx.multidex.MultiDex
import com.sjl.core.app.BaseApplication
import com.sjl.core.mvvm.BaseActivity
import com.sjl.core.net.BaseUrlAdapter
import com.sjl.core.net.RetrofitHelper
import com.sjl.core.net.RetrofitLogAdapter
import com.sjl.core.net.RetrofitParams
/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename MyApplication
 * @time 2020/12/5 13:33
 * @copyright(C) 2020 song
 */
class MyApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        initLogConfig(true)
        initRetrofitClient()
    }

    private fun initRetrofitClient() {
        RetrofitHelper.getInstance().init(RetrofitParams.Builder().setBaseUrlAdapter(object : BaseUrlAdapter {
            override fun getDefaultBaseUrl(): String {
                return "https://wanandroid.com"
            }

            override fun getAppendBaseUrl(): MutableMap<String, String>? {
                return null
            }
        }).setRetrofitLogAdapter(object : RetrofitLogAdapter{
            override fun printRequestUrl(): Boolean {
                return false
            }

            override fun printHttpLog(): Boolean {
                return true
            }
        }).setReadTimeout(10).setWriteTimeout(10).setConnectTimeout(10).build())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}