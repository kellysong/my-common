package com.sjl.lib.net


import com.sjl.lib.entity.ArticleBean
import com.sjl.lib.entity.ResponseData
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

    @GET("/wxarticle/chapters/json")
    suspend fun getDatas() : ResponseData<List<ArticleBean>>
}