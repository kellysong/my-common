package com.sjl.lib.test

import android.view.Gravity
import com.sjl.core.mvvm.BaseDialogFragment
import com.sjl.lib.R

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename BaseDialogFragment
 * @time 2021/4/13 12:02
 * @copyright(C) 2021 song
 */
class MyBaseDialogFragment : BaseDialogFragment(Gravity.BOTTOM, true) {
    override fun getLayoutResId(): Int {
        return R.layout.dialog_fragment
    }
}