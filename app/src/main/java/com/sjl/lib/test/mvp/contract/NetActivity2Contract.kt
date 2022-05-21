package com.sjl.lib.test.mvp.contract

import com.sjl.core.mvp.BaseContract
import com.sjl.core.mvp.BasePresenter
import com.sjl.lib.entity.ArticleBean

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename NetActivity2Contract
 * @time 2022/5/19 20:34
 * @copyright(C) 2022 song
 */
 interface NetActivity2Contract {

    interface View : BaseContract.IBaseView {

        fun showArticles(articleBeans: List<ArticleBean>)
        fun showFailMsg(message: String?)

    }

    abstract class Presenter :BasePresenter<View>() {

        abstract fun listArticles()
    }
}