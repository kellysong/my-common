package com.sjl.lib.test.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sjl.core.mvvm.BaseViewModel
import com.sjl.core.util.log.LogUtils
import com.sjl.lib.entity.ArticleBean
import com.sjl.lib.net.RetrofitClient
import java.lang.NullPointerException

/***
 * mvvm单向引用
 * View层指向了ViewModel层，表示View层会持有ViewModel层的引用，但是反过来ViewModel层却不能持有View层的引用。除此之外，
 * 引用也不能跨层持有，比如View层不能持有仓库层的引用，谨记每一层的组件都只能与它相邻层的组件进行交互。
 * ViewModel 实现中不得包含对 View 对象的直接引用，包括Context。
 * @property TAG (kotlin.String..kotlin.String?)
 * @property datas MutableLiveData<List<ArticleBean>>
 */
class NetViewModel : BaseViewModel() {

    private val TAG = NetViewModel::class.java.simpleName

    private val datas: MutableLiveData<List<ArticleBean>> by lazy { MutableLiveData<List<ArticleBean>>().also {
        loadDatas() } }

    /**
     * 注意到暴露的获取LiveData的方法 返回的是LiveData类型，即不可变的，而不是MutableLiveData，好处是避免数据在外部被更改
     * @return LiveData<List<ArticleBean>>
     */
    fun getArticle(): LiveData<List<ArticleBean>> {
        return datas
    }
    fun log(msg: String) = LogUtils.i("[${Thread.currentThread().name}] $msg")

    private fun loadDatas()  {

        //以前互相独立
        launchUI({
            log("child1")
            throw NullPointerException("sss")
        },{
            e ->
            LogUtils.e("获取数据异常1",e)
        })

        launchUI({
            log("child2")
        },{
            e ->
            LogUtils.e("获取数据异常2",e)
        })

        launchUI({
            log("child3")
            val api = RetrofitClient.api
            LogUtils.i("api:$api")
            val data = api.getDatas().data
            datas.value = data
        },{
            e ->
            LogUtils.e("获取数据异常",e)
        })

    }
}