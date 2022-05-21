package com.sjl.core.mvvm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 *
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BaseViewModel
 * @time 2021/1/7 10:32
 * @copyright(C) 2021 song
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {


    /**
     * 运行在UI线程的协程
     * @param block [@kotlin.ExtensionFunctionType] SuspendFunction1<CoroutineScope, T>
     * @param success SuspendFunction1<[@kotlin.ParameterName] T, Unit>?
     * @param error SuspendFunction1<[@kotlin.ParameterName] Throwable, Unit>?
     */
    fun <T : Any?> launchUI(
        block: suspend CoroutineScope.() -> T,
        success: (suspend (t: T) -> Unit)? = null,
        error: (suspend (e: Throwable) -> Unit)? = null
    ) {
        viewModelScope.launch {
            runCatching {
                block()
            }.onSuccess {
                success?.invoke(it)//带一个参数的调用
            }.onFailure {
                error?.invoke(it)
            }

        }
    }


    /**
     * 挂起函数异常捕获
     * callback?.invoke(default) 相当于  callback( default )
     * @param block SuspendFunction0<Unit>?
     * @param error SuspendFunction1<[@kotlin.ParameterName] Throwable, Unit>?
     */
    suspend fun catchException(
        block: (suspend () -> Unit)? = null,
        error: (suspend (e: Throwable) -> Unit)? = null
    ) {
        runCatching {
            block?.invoke()
        }.onFailure {
            error?.invoke(it)
        }
    }

}