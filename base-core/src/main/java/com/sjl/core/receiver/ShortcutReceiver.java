package com.sjl.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sjl.core.util.log.LogUtils;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ShortcutReceiver.java
 * @time 2019/11/17 15:31
 * @copyright(C) 2019 song
 */
public class ShortcutReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.i("快捷方式：" + intent.getAction());
    }
}
