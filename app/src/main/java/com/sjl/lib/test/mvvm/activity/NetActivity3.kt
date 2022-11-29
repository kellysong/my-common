package com.sjl.lib.test.mvvm.activity


import android.content.Intent
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.sjl.core.mvvm.BaseViewModelActivity
import com.sjl.core.util.ColorUtils
import com.sjl.lib.R
import com.sjl.lib.entity.ArticleBean
import com.sjl.lib.test.mvc.adapter.ArticleAdapter
import com.sjl.lib.test.mvvm.viewmodel.NetViewModel
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.net_activity.*
import kotlin.math.abs


const val CODE_RESULT = 100
const val KEY_RESULT = "key_result"

/**
 * MVVM示例：MVP+Retrofit+协程
 * @property articleAdapter ArticleAdapter?
 */
class NetActivity3 : BaseViewModelActivity<NetViewModel>() {

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

    }

    override fun startObserve() {
        //会触发加载数据
        //因为LiveData具有生命周期感知能力，这意味着，除非 Activity 处于活跃状态，否则它不会调用 onChanged() 回调。当调用 Activity 的 onDestroy() 方法时，LiveData 还会自动移除观察者。
        viewModel.listArticles().observe(this, Observer<List<ArticleBean>> {
            it.run {
                articleAdapter.setNewInstance(it as MutableList<ArticleBean>)
            }
        })
    }

    override fun onBackPressed() {
        val data = Intent()
        data.putExtra(KEY_RESULT, "hello world!")
        setResult(CODE_RESULT, data)
//        finish()
        super.onBackPressed()
    }


}
