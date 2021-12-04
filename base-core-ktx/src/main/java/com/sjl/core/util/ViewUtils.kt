package com.sjl.core.util

import android.content.Context
import android.view.View
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


/**
 * view点击,带有防重复点击
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    if (clickEnable()) {
        block(it as T)
    }
}

/**
 * 长按点击
 */
fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener {
    block(it as T)
}

//是否可以点击
private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

//最后点击时间
private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

//点击延迟时间，默认300ms
private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else 300
    set(value) {
        setTag(1123461123, value)
    }




/**
 * view的显示隐藏
 */
val View.isVisible: Boolean
    get() = visibility == View.VISIBLE
val View.isInVisible: Boolean
    get() = visibility == View.INVISIBLE
val View.isGone: Boolean
    get() = visibility == View.GONE