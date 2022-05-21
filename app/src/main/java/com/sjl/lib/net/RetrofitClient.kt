package com.sjl.lib.net

import com.sjl.core.net.RetrofitHelper

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename RetrofitClient
 * @time 2021/1/7 12:25
 * @copyright(C) 2021 song
 */
 object RetrofitClient {

    /**
     * 使用RetrofitHelper（单例）创建，使用协程
     */
    val api by lazy {
        return@lazy RetrofitHelper.getInstance().getApiService(Api::class.java)
    }

    /**
     * 使用RetrofitService创建，使用RxJava
     */
    val api2 by lazy {
        return@lazy RetrofitService.create<Api>()
    }

}