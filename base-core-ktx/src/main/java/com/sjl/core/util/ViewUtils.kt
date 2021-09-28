package com.sjl.core.util

import android.content.Context
import android.view.WindowManager
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.sjl.core.app.BaseApplication

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename ViewUtils
 * @time 2021/8/3 18:03
 * @copyright(C) 2021 song
 */

//资源文件相关
fun Int.getDrawable() = ActivityCompat.getDrawable(BaseApplication.getContext(), this)

fun Int.getString() = ViewUtils.getString(this)

fun Int.delay(runnable: Runnable) {
    ViewUtils.postDelayed(runnable, this.toLong())
}

fun Int.getDimen() = BaseApplication.getContext().resources.getDimension(this)


fun Int.getColor() = ViewUtils.getColor(this)

//键盘相关
fun EditText.showKeyBoard(context: Context) = ViewUtils.showKeyBoard(context, this)


fun EditText.hideKeyBoard(context: Context) = ViewUtils.hideKeyBoard(context, this)

//长度转换相关
fun Int.dp2px(context: Context, dpValue: Float) = ViewUtils.dp2px(context, dpValue)


fun Int.px2dp(context: Context, pxValue: Float) = ViewUtils.px2dp(context, pxValue)


fun Int.sp2px(context: Context, spValue: Float) = ViewUtils.px2dp(context, spValue)


fun Int.px2sp(context: Context, px: Int) = ViewUtils.px2sp(context, px)


//屏幕相关
fun Int.getScreenWidth(context: Context): Int = ViewUtils.getScreenWidth(context)


fun Int.getScreenHeight(context: Context): Int = ViewUtils.getScreenWidth(context)


fun Int.getStatusBarHeight(): Int = ViewUtils.getStatusBarHeight()


fun Int.getNavigationBarHeight(): Int = ViewUtils.getNavigationBarHeight()

