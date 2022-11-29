package com.sjl.lib.test.mvp.activity


import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.sjl.core.mvp.BaseActivity
import com.sjl.core.util.ColorUtils
import com.sjl.core.util.log.LogUtils
import com.sjl.lib.R
import com.sjl.lib.entity.ArticleBean
import com.sjl.lib.test.mvp.contract.NetActivity2Contract
import com.sjl.lib.test.mvp.presenter.NetActivity2Presenter
import com.sjl.lib.test.mvvm.activity.CODE_RESULT
import com.sjl.lib.test.mvvm.activity.KEY_RESULT
import com.sjl.lib.test.mvc.adapter.ArticleAdapter
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.net_activity.*
import kotlin.math.abs

/**
 * MVP示例：MVP+Retrofit+RxJava
 *
 * @author Kelly
 * @version 1.0.0
 * @filename NetActivity2
 * @time 2022/5/19 20:30
 * @copyright(C) 2022 song
 */
class NetActivity2 : BaseActivity<NetActivity2Presenter>(), NetActivity2Contract.View {

    override fun getLayoutId(): Int = R.layout.net_activity

    lateinit var articleAdapter: ArticleAdapter

    override fun initView() {

    }

    override fun initListener() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val color: Int = resources.getColor(R.color.colorPrimary)
            val bgColor: Int = if (color != -1) {
                ColorUtils.changeAlpha(
                    color,
                    abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange
                )
            } else {
                ColorUtils.changeAlpha(
                    resources.getColor(R.color.colorPrimary),
                    abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange
                )
            }
            setStatusBar(bgColor)
            toolbar.setBackgroundColor(bgColor)
        })
    }

    override fun initData() {
        recycleView.layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter(null)
        recycleView.adapter = articleAdapter

        mPresenter.listArticles()

    }



    override fun onBackPressed() {
        val data = Intent()
        data.putExtra(KEY_RESULT, "hello world!")
        setResult(CODE_RESULT, data)
        super.onBackPressed()
    }

    override fun showArticles(articleBeans: List<ArticleBean>) {
        articleAdapter.setNewInstance(articleBeans as MutableList<ArticleBean>)
    }

    override fun showFailMsg(message: String?) {
        LogUtils.e("获取数据异常:${message}")
    }
}
