package com.sjl.core.mvvm

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.sjl.core.kotlin.R
import com.sjl.core.util.ViewUtils

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename BaseDialogFragment
 * @time 2021/3/22 11:48
 * @copyright(C) 2021 song
 */

/**
 * @property gravity Int 位置，比如：中间，底部，直接填Gravity.BOTTOM,显示在下面
 * @property isWidth Boolean 如果是true，没有铺满屏，左右间隔20dp，里面的值可以自由设置
 * @property v View
 * @constructor
 */
abstract class BaseDialogFragment (private var gravity:Int,private var isWidth:Boolean): DialogFragment(){
    lateinit var v: View



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        v= inflater.inflate(getLayoutResId(), container, false)
        initView()
        return v
    }

    /**
     * 获取布局文件
     *
     * @return
     */
    protected abstract fun getLayoutResId(): Int

    /**
     * 初始化界面
     */
    protected open fun initView() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (dialog != null) {
            try {
                // 解决Dialog内D存泄漏
                dialog!!.setOnDismissListener(null)
                dialog!!.setOnCancelListener(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        // 全屏显示Dialog，重新测绘宽高
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        dialog?.let {
            val win = it.window
            win?.let { it ->
                it.setLayout(dm.widthPixels, win.attributes.height)
                it.setBackgroundDrawableResource(android.R.color.transparent)
            }
        }

    }

    override fun onResume() {
        //设置dialog位置和动画，样式
        dialog?.let {
            val win = it.window
            win?.let { it ->
                it.setGravity(gravity)
                val parms = it.attributes
                if (isWidth){
                    val dm = resources.displayMetrics
                    val width = dm.widthPixels
                    parms.width = width - ViewUtils.dp2px(context,20f)
                }
                win.setWindowAnimations(R.style.dialogAnim)
            }
        }
        setStyle(STYLE_NORMAL,R.style.dialogStyle)
        super.onResume()
    }




}