package com.sjl.lib.test.mvc

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.sjl.core.mvp.BaseActivity
import com.sjl.core.mvp.NoPresenter
import com.sjl.core.net.RxSchedulers
import com.sjl.core.util.ColorUtils
import com.sjl.core.util.log.LogUtils
import com.sjl.lib.R
import com.sjl.lib.entity.ArticleBean
import com.sjl.lib.net.repository.ApiRepository
import com.sjl.lib.test.mvvm.activity.CODE_RESULT
import com.sjl.lib.test.mvvm.activity.KEY_RESULT
import com.sjl.lib.test.mvc.adapter.ArticleAdapter
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.net_activity.*
import kotlin.math.abs

/**
 * MVP示例：MVC+Retrofit+RxJava
 *
 * @author Kelly
 * @version 1.0.0
 * @filename NetActivity3
 * @time 2022/5/19 21:15
 * @copyright(C) 2022 song
 */
class NetActivity : BaseActivity<NoPresenter>() {

    override fun getLayoutId(): Int = R.layout.net_activity

    lateinit var articleAdapter: ArticleAdapter

    override fun initView() {

    }

    override fun initListener() {
        toolbar.setOnClickListener {
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

    ApiRepository.listArticles2().compose(RxSchedulers.applySchedulers())
            .`as`(bindLifecycle()).subscribe(Consumer {
                showArticles(it)
            }, Consumer {
                val message = it.message
                showFailMsg(message)
            })

    }



    override fun onBackPressed() {
        val data = Intent()
        data.putExtra(KEY_RESULT, "hello world!")
        setResult(CODE_RESULT, data)
        super.onBackPressed()
    }

    fun showArticles(articleBeans: List<ArticleBean>) {
        articleAdapter.setNewInstance(articleBeans as MutableList<ArticleBean>)
    }

    fun showFailMsg(message: String?) {
        LogUtils.e("获取数据异常:${message}")
    }
}
