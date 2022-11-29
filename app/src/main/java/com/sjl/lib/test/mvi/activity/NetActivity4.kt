package com.sjl.lib.test.mvi.activity

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.sjl.core.mvvm.BaseViewModelActivity
import com.sjl.core.util.ColorUtils
import com.sjl.lib.R
import com.sjl.lib.entity.ArticleBean
import com.sjl.lib.test.mvc.adapter.ArticleAdapter
import com.sjl.lib.test.mvi.viewmodel.NetIntent
import com.sjl.lib.test.mvi.viewmodel.NetUiState
import com.sjl.lib.test.mvi.viewmodel.NetViewModel2
import com.sjl.lib.test.mvvm.activity.CODE_RESULT
import com.sjl.lib.test.mvvm.activity.KEY_RESULT
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.net_activity.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * MVVM示例：MVI+Retrofit+协程
 * @author Kelly
 * @version 1.0.0
 * @filename NetActivity4
 * @time 2022/5/19 21:21
 * @copyright(C) 2022 song
 */
class NetActivity4 : BaseViewModelActivity<NetViewModel2>() {

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

        viewModel.dispatch(NetIntent.ListArticles)
    }



    override fun startObserve() {
        lifecycleScope.launch {
            viewModel.viewState.collect {
                when (it) {
                    is NetUiState.ShowSuccess -> {
                        articleAdapter.setNewInstance(it.resData as MutableList<ArticleBean>)

                    }
                    is NetUiState.ShowError -> {
                        println("异常：${it.error.message}")
                    }
                }
            }
        }
    }


    override fun onBackPressed() {
        val data = Intent()
        data.putExtra(KEY_RESULT, "hello world!")
        setResult(CODE_RESULT, data)
//        finish()
        super.onBackPressed()
    }
}