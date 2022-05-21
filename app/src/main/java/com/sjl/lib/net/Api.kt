package com.sjl.lib.net


import com.sjl.lib.entity.ArticleBean
import com.sjl.lib.entity.ResponseData
import io.reactivex.Observable
import io.reactivex.Observer
import retrofit2.http.GET

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename Api
 * @time 2021/1/7 12:22
 * @copyright(C) 2021 song
 */
interface Api {

    /**
     * 使用协程
     * ## 使用协程,直接返回数据类对象
     * ## 不使用协程，需要返回一个Call对象使用
     * @return ResponseData<List<ArticleBean>>
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getDatas() : ResponseData<List<ArticleBean>>

    /**
     * 使用RxJava
     * @return Observable<List<ArticleBean>>
     */
    @GET("/wxarticle/chapters/json")
    fun getDatas2() : Observable<ResponseData<List<ArticleBean>>>
}