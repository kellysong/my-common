package com.sjl.core.mvvm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sjl.core.permission.PermissionsManager


/**
 *
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BaseActivity
 * @time 2021/1/7 10:32
 * @copyright(C) 2021 song
 */
abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        ready()
        initView()
        initListener()
        initData()
    }
    /**
     * 准备工作
     */
    protected open fun ready(){}

    abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()

    protected fun startActivity(z: Class<*>) {
        startActivity(Intent(applicationContext, z))
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults)
    }


}