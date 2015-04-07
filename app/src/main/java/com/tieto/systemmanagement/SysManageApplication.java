package com.tieto.systemmanagement;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by jane on 15-4-3.
 */
public class SysManageApplication extends Application {
    public static final String TYPE_WIFI = "wifi连接正常";
    public static final String TYPE_MOBILE = "手机网络连接";
    public static final String TYPE_NONE = "无网络连接";

    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;

    private static SysManageApplication ourInstance = new SysManageApplication();
    public static SysManageApplication getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }

    public String isNetConnected() {
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if(mNetworkInfo == null || !mNetworkInfo.isAvailable()) {
            return TYPE_NONE;
        } else {
            int type = mNetworkInfo.getType();
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
