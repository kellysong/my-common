package com.sjl.lib.app;

import com.sjl.core.app.BaseApplication;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename MyApplication
 * @time 2020/12/5 13:33
 * @copyright(C) 2020 song
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initLogConfig(true);
    }
}
