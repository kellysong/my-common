package com.sjl.core.mvvm

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
    protected open fun ready() {}

    abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()

    companion object {

        /**
         * 判断Activity是否Destroy
         * @param mActivity
         * @return true:已销毁
         */
        @JvmStatic
        fun isDestroy(mActivity: Activity?): Boolean {
            return mActivity == null ||
                    mActivity.isFinishing ||
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed
        }

    }

}