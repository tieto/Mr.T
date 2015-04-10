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

    /**
     * 获取当前网络状况
     * @param context
     * @return
     */
    public static String getNetworkConnectType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isAvailable()) {
            return TYPE_NONE;
        } else {
            int type = networkInfo.getType();
            switch (type) {
                case ConnectivityManager.TYPE_WIFI:
                    return TYPE_WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return TYPE_MOBILE;
            }
            return TYPE_NONE;
        }
    }
}
