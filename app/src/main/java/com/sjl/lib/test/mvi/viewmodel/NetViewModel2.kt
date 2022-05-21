package com.sjl.lib.test.mvi.viewmodel

import androidx.lifecycle.viewModelScope
import com.sjl.core.mvvm.BaseViewModel
import com.sjl.lib.entity.ArticleBean
import com.sjl.lib.net.repository.ApiRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename NetViewModel2
 * @time 2022/5/19 21:21
 * @copyright(C) 2022 song
 */
class NetViewModel2 : BaseViewModel() {

    private val _viewState = MutableSharedFlow<NetUiState>()
    val viewState: SharedFlow<NetUiState>
        get() = _viewState
    /**
     * 接收事件
     */
    private val userIntent = MutableSharedFlow<NetIntent>()

    init {
        viewModelScope.launch {
            userIntent.collect {
                when (it) {
                    is NetIntent.ListArticles -> listArticles()
                    else -> {}
                }
            }
        }
    }


    /**
     * 分发用户事件
     * @param viewAction
     */
    fun dispatch(viewAction: NetIntent) {
        try {
            viewModelScope.launch {
                userIntent.emit(viewAction)
            }
        } catch (e: Exception) {
        }
    }

    private fun listArticles() {
        launchUI({
            ApiRepository.listArticles()
        }, {
            _viewState.emit(NetUiState.ShowSuccess(it))

        }, {
            _viewState.emit(NetUiState.ShowError(it))
        })
    }

}

/**
 * 用户意图
 */
sealed class NetIntent {


    object ListArticles : NetIntent()

}

/**
 * 状态集中管理(viewEvents)
 */
sealed class NetUiState {


    /**
     * 请求失败
     * @param error 异常日志
     */
    data class ShowError(val error: Throwable) : NetUiState()

    /**
     * 请求成功
     * @param resData 返回数据
     */
    data class ShowSuccess(val resData: List<ArticleBean>) : NetUiState()

}