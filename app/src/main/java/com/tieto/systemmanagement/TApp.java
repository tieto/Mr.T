package com.tieto.systemmanagement;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by jane on 15-4-3.
 */
public class TApp extends Application {
    //TODO: put this on string file
    public static final String TYPE_WIFI = "wifi连接正常";
    public static final String TYPE_MOBILE = "手机网络连接";
    public static final String TYPE_NONE = "无网络连接";

    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;

    //TODO: change var name;
    private static TApp ourInstance;
    public static TApp getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }

    //TODO: change name and move to other class
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
