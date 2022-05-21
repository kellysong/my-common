package com.sjl.lib.net

import com.sjl.core.net.HttpLoggingInterceptor2
import com.sjl.core.util.log.LogUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitService {

    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L
    private const val CONNECT_TIMEOUT = 30L

    private const val BASE_URL = "https://www.wanandroid.com/"

    private var mRetrofit: Retrofit? = null

    private fun getClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor2(object : HttpLoggingInterceptor2.Logger {

            override fun log(message: String) {
                LogUtils.i(message)
            }
        })
        logInterceptor.level = HttpLoggingInterceptor2.Level.BODY

        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(logInterceptor) //加日志拦截器下载大文件会OOM
            .build()
    }

    fun getRetrofit(): Retrofit {
        return mRetrofit ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .validateEagerly(true)
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getClient())
            .build()
            .also { mRetrofit = it }

    }

    inline fun <reified T> create(): T = getRetrofit().create(T::class.java)

}