package com.sjl.lib.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.sjl.core.mvvm.BaseViewModel

/**
 * SavedStateHandle 其实也是通过封装 onSaveInstanceState(Bundle)和 onCreate(Bundle)两个方法来实现的，SavedStateHandle 会在 Activity 被销毁时通过onSaveInstanceState(Bundle)方法将数据保存在 Bundle 中，在重建时又将数据从 onCreate(Bundle?)中取出，开发者只负责向 SavedStateHandle 存取数据即可，并不需要和 Activity 直接做交互，从而简化了整个开发流程
 * @author Kelly
 * @version 1.0.0
 * @filename SavedStateViewModel
 * @time 2021/4/17 15:33
 * @copyright(C) 2021 song
 */
class SavedStateViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel() {

    companion object {

        private const val KEY_NAME = "savedState"

    }

    val nameLiveData = savedStateHandle.getLiveData<String>(KEY_NAME)

    val blogLiveData = MutableLiveData<String>()

}
