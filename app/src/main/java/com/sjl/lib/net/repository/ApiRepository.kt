package com.sjl.lib.net.repository

import com.sjl.lib.entity.ArticleBean
import com.sjl.lib.net.RetrofitClient
import com.sjl.lib.net.rxjava.ErrorTransformer
import io.reactivex.Observable

/**
 * 增加数据中间层(数据仓库)处理数据
 * @author Kelly
 * @version 1.0.0
 * @filename ApiRepository
 * @time 2022/5/21 22:25
 * @copyright(C) 2022 song
 */
object ApiRepository : BaseRepository() {

    /**
     * 针对协程使用
     * @return List<ArticleBean>
     */
    suspend fun listArticles(): List<ArticleBean> {
        val data = RetrofitClient.api.getDatas()
        return apiCall { data }//注意apiCall
    }


    /**
     * 针对RxJava使用
     * @return List<ArticleBean>
     */
    fun listArticles2(): Observable<List<ArticleBean>> {
        val compose = RetrofitClient.api2.getDatas2()
            .compose(ErrorTransformer())//注意compose(ErrorTransformer())
        return compose
    }

}