package com.sjl.lib.test.mvp.presenter

import com.sjl.core.net.RxSchedulers
import com.sjl.lib.net.RetrofitClient
import com.sjl.lib.test.mvp.contract.NetActivity2Contract
import io.reactivex.functions.Consumer

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename NetActivity2Presenter
 * @time 2022/5/19 20:34
 * @copyright(C) 2022 song
 */
class NetActivity2Presenter : NetActivity2Contract.Presenter() {
    override fun listArticles() {
        RetrofitClient.api2.getDatas2()
            .compose(RxSchedulers.applySchedulers())
            .`as`(bindLifecycle()).subscribe(Consumer {
                val data = it.data
                sendToView { it.showArticles(data) }
            }, Consumer {
                val message = it.message
                sendToView { it.showFailMsg(message) }
            })
    }

}