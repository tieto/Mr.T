package com.tieto.systemmanagement.trafficmonitor.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.tieto.systemmanagement.trafficmonitor.entity.AppNetWorkInfo;


/**
 * Created by jane on 15-3-25.
 */
public class AppNetInfoPreferrence {
    //sharepreferrence name for save app network state info
    public static final String PREFERRENCE_NAME = "appNetinfo";
    // key for app networking info
    private static final String APPNETSTATE = "appNetstate";
    //default value for each app's networking state
    private static final int DEFAULT = AppNetWorkInfo.NETWORK_ALLOWED.ordinal();


    public static void saveAppNetState(Context ctx, int uid,int value) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERRENCE_NAME, Context.MODE_PRIVATE);
        pref.edit().putInt(APPNETSTATE+uid,value).commit();
    }

    public static int getAppNetState(Context ctx,int uid) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERRENCE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(APPNETSTATE+uid,DEFAULT);
    }

    public static void clear(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERRENCE_NAME, Context.MODE_PRIVATE);
        pref.edit().clear().commit();
    }


}
