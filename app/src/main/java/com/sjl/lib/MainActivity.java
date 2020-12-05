package com.sjl.lib;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import com.sjl.core.mvp.BaseActivity;
import com.sjl.core.permission.PermissionsManager;
import com.sjl.core.permission.PermissionsResultAction;
import com.sjl.core.util.ViewUtils;
import com.sjl.lib.test.LogTestActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_msg)
    TextView tv_msg;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tv_msg.setText("hello world！ my-common-lib");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        PermissionsManager.getInstance()
                .requestPermissionsIfNecessaryForResult(this, permissions, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {

            }
        });
    }

    public void btnTestLog(View view) {
        ViewUtils.openActivity(this, LogTestActivity.class);
    }
}
