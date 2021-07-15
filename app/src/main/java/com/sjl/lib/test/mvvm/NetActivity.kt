package com.sjl.lib.test.mvvm


import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sjl.core.mvvm.BaseViewModelActivity
import com.sjl.lib.R
import com.sjl.lib.entity.ArticleBean
import kotlinx.android.synthetic.main.content_scrolling.*


class NetActivity : BaseViewModelActivity<NetViewModel>() {

    override fun getLayoutId(): Int = R.layout.net_activity

    override fun providerVMClass(): Class<NetViewModel>? = NetViewModel::class.java

    private val TAG = this.javaClass.simpleName
    var createAdapter: ArticleAdapter? = null

    override fun initView() {
        recycleView.layoutManager = LinearLayoutManager(this)
        createAdapter = createAdapter()
        recycleView.adapter = createAdapter
    }

    override fun initListener() {

    }

    override fun initData() {
        //会触发加载数据
        //因为LiveData具有生命周期感知能力，这意味着，除非 Activity 处于活跃状态，否则它不会调用 onChanged() 回调。当调用 Activity 的 onDestroy() 方法时，LiveData 还会自动移除观察者。
        viewModel.getArticle().observe(this, Observer<List<ArticleBean>> {
            it?.run {
                createAdapter?.setNewInstance(it as MutableList<ArticleBean>?)
            }
        })

    }

    private fun createAdapter(): ArticleAdapter {
        return ArticleAdapter(null)
    }
}
