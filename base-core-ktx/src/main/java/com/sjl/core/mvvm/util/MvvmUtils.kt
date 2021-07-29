package com.sjl.core.mvvm.util

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.sjl.core.mvvm.BaseActivity
import com.sjl.core.mvvm.BaseViewModel
import com.sjl.core.mvvm.BaseViewModelFragment
import com.sjl.core.util.ToastUtils

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename MvvmUtils
 * @time 2021/7/29 14:54
 * @copyright(C) 2021 song
 */
/**
 * 通过Class跳转界面
 */
fun BaseActivity.openActivity(cls: Class<*>?) {
    openActivity(cls, null)
}

/**
 * 含有Bundle通过Class跳转界面
 */
fun BaseActivity.openActivity(cls: Class<*>?, bundle: Bundle?) {
    val intent = Intent()
    intent.setClass(this, cls!!)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}


/**
 * 通过Class跳转界面(有回调)
 */
fun BaseActivity.openActivityForResult(cls: Class<*>?, requestCode: Int) {
    openActivityForResult(cls, null, requestCode)
}

/**
 * 含有Bundle通过Class跳转界面(有回调)
 */
fun BaseActivity.openActivityForResult(cls: Class<*>?, bundle: Bundle?,
                                       requestCode: Int) {
    val intent = Intent()
    intent.setClass(this, cls!!)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivityForResult(intent, requestCode)
}


/**
 * 短暂显示Toast提示(id)
 */
fun BaseActivity.showShortToast(resId: Int) {
    ToastUtils.showShort(this, resId)
}

/**
 * 短暂显示Toast提示(来自String)
 */
fun BaseActivity.showShortToast(text: String?) {
    ToastUtils.showShort(this, text)
}


/**
 * 长时间显示Toast提示(id)
 */
fun BaseActivity.showLongToast(resId: Int) {
    ToastUtils.showLong(this, resId)
}

/**
 * 长时间显示Toast提示(来自String)
 */
fun BaseActivity.showLongToast(text: String?) {
    ToastUtils.showLong(this, text)
}


/**
 * 通过Class跳转界面
 */
fun BaseViewModelFragment<BaseViewModel>.openActivity(cls: Class<*>?) {
    openActivity(cls, null)
}

/**
 * 含有Bundle通过Class跳转界面
 */
fun BaseViewModelFragment<BaseViewModel>.openActivity(cls: Class<*>?, bundle: Bundle?) {
    val intent = Intent()
    activity?.let { intent.setClass(it, cls!!) }
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}


/**
 * 通过Class跳转界面(有回调)
 */
fun BaseViewModelFragment<BaseViewModel>.openActivityForResult(cls: Class<*>?, requestCode: Int) {
    openActivityForResult(cls, null, requestCode)
}

/**
 * 含有Bundle通过Class跳转界面(有回调)
 */
fun BaseViewModelFragment<BaseViewModel>.openActivityForResult(cls: Class<*>?, bundle: Bundle?,
                                                               requestCode: Int) {
    val intent = Intent()
    activity?.let { intent.setClass(it, cls!!) }
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivityForResult(intent, requestCode)
}


/**
 * 短暂显示Toast提示(id)
 */
fun BaseViewModelFragment<BaseViewModel>.showShortToast(resId: Int) {
    ToastUtils.showShort(activity, resId)
}

/**
 * 短暂显示Toast提示(来自String)
 */
fun BaseViewModelFragment<BaseViewModel>.showShortToast(text: String?) {
    ToastUtils.showShort(activity, text)
}


/**
 * 长时间显示Toast提示(id)
 */
fun BaseViewModelFragment<BaseViewModel>.showLongToast(resId: Int) {
    ToastUtils.showLong(activity, resId)
}

/**
 * 长时间显示Toast提示(来自String)
 */
fun BaseViewModelFragment<BaseViewModel>.showLongToast(text: String?) {
    ToastUtils.showLong(activity, text)
}