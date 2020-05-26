package com.sjl.lib;

import android.os.Bundle;
import android.widget.TextView;

import com.sjl.core.util.log.LogUtils;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tv_msg.setText("hello world！ my-common-lib");
        LogUtils.i("你好");
    }
}
