package com.sjl.lib.test.mvvm.adapter

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

    override fun convert(holder: BaseViewHolder, item: ArticleBean) {
        with(holder) {
            setText(R.id.tv_name, item.name)
                    .setText(R.id.tv_role,item.id.toString())
        }
    }

}