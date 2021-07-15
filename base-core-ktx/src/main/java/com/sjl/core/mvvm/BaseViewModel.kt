package com.sjl.core.mvvm

import androidx.lifecycle.*
import com.sjl.core.util.log.LogUtils
import kotlinx.coroutines.*
import java.lang.Exception


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
     * @param block [@kotlin.ExtensionFunctionType] SuspendFunction1<CoroutineScope, Unit>
     * @param error Function1<[@kotlin.ParameterName] Throwable, Unit>?
     * @param finally Function0<Unit>?
     * @return Job
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit, error: (suspend (e: Throwable) -> Unit)? = null, finally: (suspend () -> Unit)? = null) = viewModelScope.launch {
        var tempE: Exception? = null
        try {
//            withTimeout(10_1000){ //协程10s超时
//                block()
//            }
            block()
        } catch (e: CancellationException) {
            LogUtils.e("launchUI,job cancelled")
        } catch (e: Exception) {
            tempE = e
            if (error != null) {
                error(tempE)
            }
        } finally {//命令式
            if (tempE == null) {
                finally?.invoke()
            }
        }

    }

    /**
     * 协程异常处理
     *  //callback?.invoke(default) 相当于  callback( default )
    //(suspend () -> Unit)? = null
     * @param block SuspendFunction0<Unit>?
     * @param error SuspendFunction1<[@kotlin.ParameterName] Throwable, Unit>?
     */
    suspend fun catchException(block: (suspend () -> Unit)? = null, error: (suspend (e: Throwable) -> Unit)? = null) {
        try {
            block?.invoke()
        } catch (e: Exception) {
            if (error != null) {
                error(e)
            }
        }
    }


    /**
     * 如果不用 viewModelScope 我们就得重写的 onCleared() 方法。在 clear() 方法中，ViewModel 会取消 viewModelScope 中的任务
     */
    /*   override fun onCleared() {
           super.onCleared()
           viewModelScope.cancel()
       }*/
}