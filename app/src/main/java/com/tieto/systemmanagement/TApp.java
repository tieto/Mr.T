package com.tieto.systemmanagement;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by jane on 15-4-3.
 */
public class TApp extends Application {
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
}
