package com.sjl.core.util

import com.sjl.core.util.log.LogUtils

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename LogUtils
 * @time 2021/8/3 17:39
 * @copyright(C) 2021 song
 */

fun String.v() =  LogUtils.v(this)


fun String.d() = LogUtils.d(this)

fun String.i()=LogUtils.i(this)


@JvmOverloads
fun String.w(tr: Throwable? = null) {
    LogUtils.w(this,tr)
}

@JvmOverloads
fun String.e(tr: Throwable? = null) {
    LogUtils.e(this,tr)
}
