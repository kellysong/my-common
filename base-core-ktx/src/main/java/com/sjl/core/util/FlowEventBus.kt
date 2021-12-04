package com.sjl.core.util

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * kotlin flow事件总线
 * 参考：
 * https://blog.csdn.net/qq474731835/article/details/119893499
 * https://github.com/biubiuqiu0/flow-event-bus/blob/master/core/src/main/java/com/biubiu/eventbus/core/EventBusCore.kt
 */
object FlowEventBus {
    private const val TAG = "FlowEventBus"
    private val busMap = mutableMapOf<Int, EventBus<*>>()

    /**
     *
     * @param key Int 事件key,尽量唯一
     * @param isSticky Boolean 是否开启粘性
     * @return EventBus<T>
     */
    @Synchronized
    fun <T> with(key: Int, isSticky: Boolean): EventBus<T> {
        var eventBus = busMap[key]
        if (eventBus == null) {
            eventBus = EventBus<T>(key, isSticky)
            busMap[key] = eventBus
        }
        return eventBus as EventBus<T>
    }

    fun <T> with(key: Int): EventBus<T> {
        return with(key, false)
    }

    /**
     *
     * @param T
     * @property key Int
     * @property _events MutableSharedFlow<T>
     * @property events SharedFlow<T>
     * @constructor
     */
    class EventBus<T>(private val key: Int, isSticky: Boolean) : LifecycleObserver {
        /**
         * replay：是否接受collect前的数据，默认为0，不会接收以前的数据
            extraBufferCapacity：减去replay后的缓冲空间
            onBufferOverflow：缓存策略，即缓冲区装满后Flow处理方式，默认为挂起
         */
        private val _events = MutableSharedFlow<T>(replay = if (isSticky) 1 else 0,extraBufferCapacity = Int.MAX_VALUE) // private mutable shared flow
        val events = _events.asSharedFlow() // publicly exposed as read-only shared flow


        /**
         * need main thread execute
         */
        fun register(lifecycleOwner: LifecycleOwner, action: (t: T) -> Unit) {
            lifecycleOwner.lifecycle.addObserver(this)
            lifecycleOwner.lifecycleScope.launch {
                events.collect {
                    try {
                        action(it)
                    } catch (e: Exception) {
                        Log.e(TAG, "KEY:%s---ERROR:%s".format(key, e.toString()))
                    }
                }
            }
        }

        /**
         * send value on main thread
         */
        fun post(event: T, timeMillis: Long = 0) {
            MainScope().launch {
                delay(timeMillis)
                _events.emit(event)
            }
        }

        /**
         * When subscriptionCount less than 0,remove event object in map
         */
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            val subscriptCount = _events.subscriptionCount.value
            if (subscriptCount <= 0)
                busMap.remove(key)
        }
    }


}