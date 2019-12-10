package com.sjl.core.widget.loadingdialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjl.core.R;


/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename LoadingDialog.java
 * @time 2018/7/11 9:45
 * @copyright(C) 2018 song
 */
public class LoadingDialog {
    LVCircularRing mLoadingView;
    Dialog mLoadingDialog;
    TextView loadingText;

    public LoadingDialog(Context context, String msg) {
        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.loading_dialog_view, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.dialog_view);
        // 页面中的LoadingView
        mLoadingView = (LVCircularRing) view.findViewById(R.id.lv_circularring);
        // 页面中显示文本
        loadingText = (TextView) view.findViewById(R.id.loading_text);
        // 显示文本
        loadingText.setText(msg);
        // 创建自定义样式的Dialog
        mLoadingDialog = new Dialog(context, R.style.loading_dialog);
        // 设置返回键无效
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void show() {
        mLoadingDialog.show();
        mLoadingView.startAnim();
    }

    /**
     * 设置加载文本
     *
     * @param loadingMsg
     */
    public void setLoadingMsg(String loadingMsg) {
        if (loadingText != null) {
            if (TextUtils.isEmpty(loadingMsg)) {
                loadingText.setVisibility(View.GONE);
            } else {
                loadingText.setVisibility(View.VISIBLE);
            }
            loadingText.setText(loadingMsg);
        }
    }

    public void close() {
        if (mLoadingDialog != null) {
            mLoadingView.stopAnim();
            mLoadingDialog.dismiss();
        }
    }
}

