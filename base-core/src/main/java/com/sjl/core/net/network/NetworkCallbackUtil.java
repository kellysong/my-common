package com.sjl.core.net.network;

import android.content.Context;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * 监听网络连接状态
 * 主要作用类还是 NetworkCallbackImpl
 */
public class NetworkCallbackUtil {

    /**
     * 在application中调用
     */
    public static void register(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkCallbackImpl.getInstance().register(context);
        } else {
            NetworkChangBroadcast.getInstance().register(context);
        }
    }

    public static void addNetworkCallback(Object tag, ConnectivityChangeCallback call) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkCallbackImpl.getInstance().addNetworkCallback(tag, call);
        } else {
            NetworkChangBroadcast.getInstance().addNetworkCallback(tag, call);
        }
    }

    public static void removeNetworkCallback(Object tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkCallbackImpl.getInstance().removeNetworkCallback(tag);
        } else {
            NetworkChangBroadcast.getInstance().removeNetworkCallback(tag);
        }
    }


    /**
     * 网络状态监听接口
     *
     */
    public interface ConnectivityChangeCallback {
        void onAvailable(int networkType);

        void onLost(int networkType);
    }
}
