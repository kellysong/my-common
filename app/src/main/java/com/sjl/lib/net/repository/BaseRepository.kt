package com.sjl.lib.net.repository

import com.sjl.lib.entity.ResponseData
import com.sjl.lib.net.DataResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename BaseRepository
 * @time @time 2022/5/21 22:10
 * @copyright(C) 2022 song
 */
open class BaseRepository {
    /**
     *  0代表请求成功，统一处理请求,避免在每次网络请求的时候对status进行重复判断
     *
     * @param call SuspendFunction0<BaseData<T>>
     * @return BaseData<T>
     */
    suspend fun <T : Any> apiCallWithIO(call: suspend () -> ResponseData<T>): T {
        val apply = withContext(Dispatchers.IO) { call.invoke() }.apply {
            //根据错误码处理
            when (errorCode) {
                0 -> {

                }
                else  -> throw DataResponseException(errorCode,errorMsg)
            }
        }
        return apply.data
    }

    /**
     *  0代表请求成功，统一处理请求,避免在每次网络请求的时候对status进行重复判断
     * @param call Function0<ResponseData<T>>
     * @return T
     */
    fun <T : Any> apiCall(call: () -> ResponseData<T>): T {
        val apply = call.invoke().apply {
            //根据错误码处理
            when (errorCode) {
                0 -> {

                }
                else  -> throw DataResponseException(errorCode,errorMsg)
            }
        }
        return apply.data
    }

}
