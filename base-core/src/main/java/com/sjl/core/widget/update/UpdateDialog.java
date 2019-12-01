/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sjl.core.widget.update;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjl.core.R;
import com.sjl.core.entity.dto.UpdateInfoDto;
import com.sjl.core.util.ColorUtils;
import com.sjl.core.util.DrawableUtils;
import com.sjl.core.util.ViewUtils;


/**
 * 版本更新弹窗
 */
public class UpdateDialog extends BaseDialog implements View.OnClickListener {

    //======顶部========//
    /**
     * 顶部图片
     */
    private ImageView mIvTop;
    /**
     * 标题
     */
    private TextView mTvTitle;
    //======更新内容========//
    /**
     * 版本更新内容
     */
    private TextView mTvUpdateInfo;
    /**
     * 版本更新
     */
    private Button mBtnUpdate;
    /**
     * 后台更新
     */
    private Button mBtnBackgroundUpdate;
    /**
     * 忽略版本
     */
    private TextView mTvIgnore;
    /**
     * 进度条
     */
    private NumberProgressBar mNumberProgressBar;
    //======底部========//
    /**
     * 底部关闭
     */
    private LinearLayout mLlClose;
    private ImageView mIvClose;


    private UpdateDialogListener updateDialogListener;


    /**
     * 初始化更新弹框
     *
     * @param context
     * @param updateInfoDto
     * @param colorResId
     * @param topResId
     * @return
     */
    public static UpdateDialog newInstance(Context context, UpdateInfoDto updateInfoDto, @ColorRes int colorResId, @DrawableRes int topResId) {
        UpdateDialog dialog = new UpdateDialog(context);
        dialog.setUpdateEntity(updateInfoDto);
        dialog.initTheme(colorResId, topResId);
        return dialog;
    }

    /**
     * 初始化更新弹框
     *
     * @param context
     * @param updateInfoDto
     * @return
     */
    public static UpdateDialog newInstance(Context context, UpdateInfoDto updateInfoDto) {
        UpdateDialog dialog = new UpdateDialog(context);
        dialog.setUpdateEntity(updateInfoDto);
        dialog.initTheme(-1, -1);
        return dialog;
    }

    public UpdateDialog setUpdateDialogListener(UpdateDialogListener updateDialogListener) {
        this.updateDialogListener = updateDialogListener;
        return this;
    }


    private UpdateDialog(Context context) {
        super(context, R.layout.xupdate_dialog_app);
    }


    @Override
    protected void initViews() {
        //顶部图片
        mIvTop = findViewById(R.id.iv_top);
        //标题
        mTvTitle = findViewById(R.id.tv_title);
        //提示内容
        mTvUpdateInfo = findViewById(R.id.tv_update_info);
        //更新按钮
        mBtnUpdate = findViewById(R.id.btn_update);
        //后台更新按钮
        mBtnBackgroundUpdate = findViewById(R.id.btn_background_update);
        //忽略
        mTvIgnore = findViewById(R.id.tv_ignore);
        //进度条
        mNumberProgressBar = findViewById(R.id.npb_progress);

        //关闭按钮+线 的整个布局
        mLlClose = findViewById(R.id.ll_close);
        //关闭按钮
        mIvClose = findViewById(R.id.iv_close);
    }

    @Override
    protected void initListeners() {
        mBtnUpdate.setOnClickListener(this);
        mBtnBackgroundUpdate.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mTvIgnore.setOnClickListener(this);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }


    //====================UI构建============================//

    public UpdateDialog setUpdateEntity(UpdateInfoDto updateEntity) {
        initUpdateInfo(updateEntity);
        return this;
    }

    /**
     * 初始化更新信息
     *
     * @param updateInfoDto
     */
    private void initUpdateInfo(UpdateInfoDto updateInfoDto) {
        //弹出对话框
        final String newVersion = updateInfoDto.getVersionName();
        String updateInfo = getDisplayUpdateInfo(getContext(), updateInfoDto);
        //更新内容
        mTvUpdateInfo.setText(updateInfo);
        mTvTitle.setText(String.format(getString(R.string.xupdate_lab_ready_update), newVersion));

        //强制更新,不显示关闭按钮
        if (updateInfoDto.isForce()) {
            mLlClose.setVisibility(View.GONE);
        } else {
            //不是强制更新时，才生效
            if (updateInfoDto.isIgnorable()) {
                mTvIgnore.setVisibility(View.VISIBLE);
            }
        }
    }

    public static String getDisplayUpdateInfo(Context context, UpdateInfoDto updateInfoDto) {
        String targetSize = Formatter.formatShortFileSize(context, updateInfoDto.getSize());
        final String updateContent = updateInfoDto.getUpdateContent();

        String updateInfo = "";
        if (!TextUtils.isEmpty(targetSize)) {
            updateInfo = context.getString(R.string.xupdate_lab_new_version_size) + targetSize + "\n\r";
        }
        if (!TextUtils.isEmpty(updateContent)) {
            updateInfo += updateContent;
        }
        return updateInfo;
    }


    /**
     * 初始化主题色
     *
     * @param colorResId 主色
     * @param topResId   图片
     */
    private void initTheme(@ColorRes int colorResId, @DrawableRes int topResId) {
        int color;
        if (colorResId == -1) {
            color = ViewUtils.getColor(R.color.xupdate_default_theme_color);
        } else {
            color = ViewUtils.getColor(colorResId);

        }
        if (topResId == -1) {
            topResId = R.mipmap.xupdate_bg_app_top;
        }
        mIvTop.setImageResource(topResId);
        mBtnUpdate.setBackgroundDrawable(DrawableUtils.getDrawable(ViewUtils.dp2px(getContext(), 4), color));
        mBtnBackgroundUpdate.setBackgroundDrawable(DrawableUtils.getDrawable(ViewUtils.dp2px(getContext(), 4), color));
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        mBtnUpdate.setTextColor(ColorUtils.isColorDark(color) ? Color.WHITE : Color.BLACK);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        //点击版本升级按钮【下载apk】
        if (i == R.id.btn_update) {
            if (updateDialogListener != null) {
                updateDialogListener.onUpdate(this);
            }
        } else if (i == R.id.btn_background_update) {
            //点击后台更新按钮 TODO:待做
            dismiss();
        } else if (i == R.id.iv_close) {
            //点击关闭按钮
            if (updateDialogListener != null) {
                updateDialogListener.onCancel(this);
            }
        } else if (i == R.id.tv_ignore) {
            //点击忽略按钮 TODO:待做
            dismiss();
        }
    }


}
