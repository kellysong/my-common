package com.sjl.lib.test.mvvm

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sjl.lib.R
import com.sjl.lib.entity.ArticleBean


/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ArticleAdapter
 * @time 2021/1/7 12:02
 * @copyright(C) 2021 song
 */
class ArticleAdapter(data: List<ArticleBean>?) : BaseQuickAdapter<ArticleBean, BaseViewHolder>(R.layout.article_recycle_item, data as MutableList<ArticleBean>?) {
    override fun convert(baseViewHolder: BaseViewHolder, articleBean: ArticleBean) {
        with(baseViewHolder) {
            setText(R.id.tv_name, articleBean.name)
                    .setText(R.id.tv_role,articleBean.id.toString())
        }
    }
}