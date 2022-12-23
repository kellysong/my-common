package com.sjl.lib.net

import com.sjl.core.net.RetrofitHelper

/**
 * Http客户端
 * @author Kelly
 * @version 1.0.0
 * @filename RetrofitClient
 * @time 2021/1/7 12:25
 * @copyright(C) 2021 song
 */
 object RetrofitClient {

    /**
     * 使用RetrofitHelper（单例）创建，支持协程、RxJava
     */
    val api by lazy {
        return@lazy RetrofitHelper.getInstance().getApiService(Api::class.java)
    }


}