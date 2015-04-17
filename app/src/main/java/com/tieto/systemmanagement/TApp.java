package com.tieto.systemmanagement;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.tieto.systemmanagement.trafficmonitor.service.CalculateBackGroundTrafficService;

/**
 * Created by jane on 15-4-3.
 */
public class TApp extends Application {

    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;
    //if the application is first installed
    private boolean isFirstInstalled;

    private static final String PREF_NAME = "SystemManagePref";
    private static final String KEY_FIRST_INSTALLED = "KeyFirstInstalled";

    //TODO: change var name;
    private static TApp ourInstance;
    public static TApp getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;

        IsFirstInstalled();

    }
    // check if it's first installed
    private void IsFirstInstalled() {
        SharedPreferences pref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        isFirstInstalled = pref.getBoolean(KEY_FIRST_INSTALLED,true);
        if(isFirstInstalled) {
            Intent intent = new Intent();
            intent.setClass(this,CalculateBackGroundTrafficService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startService(intent);

            pref.edit().putBoolean(KEY_FIRST_INSTALLED,false).commit();
        }
    }

}
