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

    val api by lazy {
        return@lazy RetrofitHelper.getInstance().getApiService(Api::class.java)
    }

}