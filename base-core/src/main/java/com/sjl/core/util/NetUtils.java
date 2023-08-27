package com.sjl.core.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.sjl.core.net.network.NetworkType;

import java.io.IOException;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

/**
 *
 *
 * @author Kelly
 * @version 1.0.0
 * @filename NetUtils
 * @time 2023/5/17 11:56
 * @copyright(C) 2023 song
 */
public class NetUtils {

    private static final String TAG = "NetUtils";

    /**
     * 配置WiFi
     *
     * @param context
     * @param ssid
     * @param password
     * @return
     */
    public static boolean configureWifi(Context context, String ssid, String password) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID = "\"" + ssid + "\"";
        wifiConfiguration.preSharedKey = "\"" + password + "\"";
        wifiConfiguration.hiddenSSID = false;
        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
        //移除
        WifiConfiguration tempConfig = isExist(context, ssid);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }
        int netId = wifiManager.addNetwork(wifiConfiguration);
        boolean b = wifiManager.enableNetwork(netId, true);
        boolean connected = wifiManager.reconnect();
        Log.d(TAG, "enableNetwork connected=" + connected);
        return b;
    }

    /**
     * 查看以前是否也配置过这个网络
     * @param context
     * @param ssid
     * @return
     */
    public static WifiConfiguration isExist(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            // 设备不支持WiFi功能
            return null;
        }
        @SuppressLint("MissingPermission") List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (CollectionUtils.isEmpty(existingConfigs)) {
            return null;
        }
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + ssid + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * 检查WiFi是否可用，若未开启则请求开启WiFi
     * @param context
     */
    public static void enableWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            // 设备不支持WiFi功能
            return;
        }
        if (!wifiManager.isWifiEnabled()) {
            // 未开启WiFi，请求开启
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 判断wifi是否打开
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            // 设备不支持WiFi功能
            return false;
        }
        return wifiManager.isWifiEnabled();
    }

    /**
     * 检测网络是否连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());

    }


    /**
     * 检测网络连通性(是否能访问网络)
     * @param context
     * @return
     */
    public static boolean isNetworkOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);

            }
        }
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 3 www.baidu.com");

            int exitValue = ipProcess.waitFor();
            Log.i("Avalible", "Process:" + exitValue);
            ipProcess.destroy();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        }

        return false;

    }

    public static int getNetworkType(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            return NetworkType.OTHER;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetworkType.WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return NetworkType.MOBILE;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            return NetworkType.ETHERNET;
        } else {
            return NetworkType.OTHER;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static int getNetworkType(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities == null) {
            return NetworkType.OTHER;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return NetworkType.WIFI;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return NetworkType.MOBILE;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return NetworkType.ETHERNET;
        } else {
            return NetworkType.OTHER;
        }
    }


    /**
     * 当前网络连接的wifi名称
     * @param context
     * @return
     */
    public static String getWifiName(Context context) {
        String ssid;
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        ssid = info != null ? info.getSSID() : null;
        if (!TextUtils.isEmpty(ssid)) {
            if (ssid.contains("\"")) {
                ssid = ssid.replace("\"", "");
            }
            return ssid;
        }
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (null != mWifiManager) {
            int networkId = info.getNetworkId();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            List<WifiConfiguration> netConfList = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration wifiConfiguration : netConfList) {
                if (wifiConfiguration.networkId == networkId) {
                    ssid = wifiConfiguration.SSID;
                    break;
                }
            }
        }
        if (ssid.contains("\"")) {
            ssid = ssid.replace("\"", "");
        }
        return ssid;
    }

}
