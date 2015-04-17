package com.tieto.systemmanagement.trafficmonitor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by jane on 15-4-10.
 */
public class NetworkUtil {

    public static final String TYPE_WIFI = "wifi连接正常";
    public static final String TYPE_MOBILE = "手机网络连接";
    public static final String TYPE_NONE = "无网络连接";

    private static ConnectivityManager mConnectivityManager;
    private static NetworkInfo mNetworkInfo;

    /**
     * 获取当前网络连接类型
     * @param context
     * @return
     */
    public static String getNetworkConnectType(Context context) {
        if (isNetworkConnected(context)) {
            int type = mNetworkInfo.getType();
            switch (type) {
                case ConnectivityManager.TYPE_WIFI:
                    return TYPE_WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return TYPE_MOBILE;
            }
            return TYPE_NONE;
        }
        return TYPE_NONE;
    }

    /**
     * check if the network is accessible
     * @param context
     * @return true if network is accessible, or false
     */
    public static boolean isNetworkConnected(Context context) {
        mConnectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if(mNetworkInfo == null || !mNetworkInfo.isAvailable()) {
            return false;
        }
        return true;
    }
}
