package com.sjl.lib.test.log;

import com.google.gson.Gson;
import com.sjl.core.mvp.BaseActivity;
import com.sjl.core.util.log.LogUtils;
import com.sjl.core.util.log.LogWriter;
import com.sjl.lib.R;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename LogTestActivity
 * @time 2020/12/5 13:40
 * @copyright(C) 2020 song
 */
public class LogTestActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.log_test_activity;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

        LogUtils.i(getJson(1));
        LogUtils.json(getJson(3));
        LogUtils.w("ww", new RuntimeException("出错了"));
        LogUtils.e("ww", new RuntimeException("出错了2"));
        LogUtils.i("==========================");


        LogWriter.i(getJson(1));
        LogWriter.json(getJson(3));
        LogWriter.w("ww", new RuntimeException("出错了"));
        LogWriter.json(getJson(1000));
        LogWriter.xml(getXml());

    /*    LogKtxKt.w("dd");
        LogKtxKt.w("dd",new RuntimeException());*/
    }

    private String getXml() {
        String xml="<bookstore>\n" +
                "<book category=\"CHILDREN\">\n" +
                "  <title>Harry Potter</title> \n" +
                "  <author>J K. Rowling</author> \n" +
                "  <year>2005</year> \n" +
                "  <price>29.99</price> \n" +
                "</book>\n" +
                "<book category=\"WEB\">\n" +
                "  <title>Learning XML</title> \n" +
                "  <author>Erik T. Ray</author> \n" +
                "  <year>2003</year> \n" +
                "  <price>39.95</price> \n" +
                "</book>\n" +
                "</bookstore> ";
        return xml;
    }

    private String getJson(int i) {
        Map<Integer, String> map = new LinkedHashMap<>();
        for (int j = 0; j < i; j++) {
            map.put(j, String.valueOf(System.currentTimeMillis()));
        }
        return new Gson().toJson(map);
    }
}
