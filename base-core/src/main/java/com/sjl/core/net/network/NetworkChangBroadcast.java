package com.sjl.core.net.network;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.sjl.core.util.NetUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkChangBroadcast {
    private BroadcastReceiver mNetworkChangBroadcast;
    private Map<Object, NetworkCallbackUtil.ConnectivityChangeCallback> callbackList = new ConcurrentHashMap<>();

    private static class SingletonHolder {
        static final NetworkChangBroadcast INSTANCE = new NetworkChangBroadcast();
    }

    public static NetworkChangBroadcast getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private NetworkChangBroadcast() {
        mNetworkChangBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                int networkType = NetUtils.getNetworkType(networkInfo);
                if (networkInfo != null && networkInfo.isAvailable()) {

//            Toast.makeText(context, "网络已连接", Toast.LENGTH_SHORT).show();
                    for (NetworkCallbackUtil.ConnectivityChangeCallback callback : callbackList.values()) {
                        callback.onAvailable(networkType);
                    }
                } else {
//            Toast.makeText(context, "网络断开", Toast.LENGTH_SHORT).show();
                    for (NetworkCallbackUtil.ConnectivityChangeCallback callback : callbackList.values()) {
                        callback.onLost(networkType);
                    }
                }
            }
        };
    }

    public void register(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(mNetworkChangBroadcast, intentFilter);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(mNetworkChangBroadcast);
    }

    public void addNetworkCallback(Object tag, NetworkCallbackUtil.ConnectivityChangeCallback call) {
        callbackList.put(tag, call);
    }

    public void removeNetworkCallback(Object tag) {
        callbackList.remove(tag);
    }


}
