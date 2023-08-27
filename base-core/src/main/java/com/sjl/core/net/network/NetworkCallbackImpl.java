package com.sjl.core.net.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;


import com.sjl.core.util.NetUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl {
    private ConnectivityManager.NetworkCallback networkCallback;
    private Map<Object, NetworkCallbackUtil.ConnectivityChangeCallback> callbackList = new ConcurrentHashMap<>();
    private ConnectivityManager mConnectivityManager;
    private NetworkCallbackImpl() {
        networkCallback = new ConnectivityManager.NetworkCallback() {
            /**
             * 网络可用的回调
             */
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                NetworkCapabilities networkCapabilities = mConnectivityManager.getNetworkCapabilities(network);
                int netType = NetUtils.getNetworkType(networkCapabilities);

                for (NetworkCallbackUtil.ConnectivityChangeCallback callback : callbackList.values()) {
                    callback.onAvailable(netType);
                }
            }

            /**
             * 在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调
             */
            @Override
            public void onLosing(Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
            }

            /**
             * 网络丢失的回调
             */
            @Override
            public void onLost(Network network) {
                super.onLost(network);
                NetworkCapabilities networkCapabilities = mConnectivityManager.getNetworkCapabilities(network);
                int netType = NetUtils.getNetworkType(networkCapabilities);
                for (NetworkCallbackUtil.ConnectivityChangeCallback callback : callbackList.values()) {
                    callback.onLost(netType);
                }
            }

            /**
             * 按照官方的字面意思是，当我们的网络的某个能力发生了变化回调，那么也就是说可能会回调多次
             * <p>
             * 之后在仔细的研究
             */
            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
            }

            /**
             * 当建立网络连接时，回调连接的属性
             */
            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
            }

            /**
             * 按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
             */
            @Override
            public void onUnavailable() {
                super.onUnavailable();
            }
        };
    }

    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        public static final NetworkCallbackImpl INSTANCE = new NetworkCallbackImpl();
    }

    public static NetworkCallbackImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 在application中调用
     */
    public void register(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 请注意这里会有一个版本适配bug，所以请在这里添加非空判断
            if (mConnectivityManager != null) {
                mConnectivityManager.registerNetworkCallback(request, networkCallback);
            }
        }
    }

    public void addNetworkCallback(Object tag, NetworkCallbackUtil.ConnectivityChangeCallback call) {
        callbackList.put(tag, call);
    }

    public void removeNetworkCallback(Object tag) {
        callbackList.remove(tag);
    }

}