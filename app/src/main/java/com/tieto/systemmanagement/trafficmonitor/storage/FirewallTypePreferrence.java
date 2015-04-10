package com.tieto.systemmanagement.trafficmonitor.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.tieto.systemmanagement.trafficmonitor.entity.FirewallType;


/**
 * Created by jane on 15-3-25.
 */
public class FirewallTypePreferrence {
    //sharepreferrence name for save app network state info
    public static final String PREFERRENCE_NAME = "appNetinfo";
    // key for app networking info
    private static final String NETWORK_STATE = "appNetstate";
    //default value for each app's networking state
    private static final int DEFAULT = FirewallType.NETWORK_ALLOWED.ordinal();

    public static void saveNetworkState(Context ctx, int uid, int value) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERRENCE_NAME, Context.MODE_PRIVATE);
        pref.edit().putInt(NETWORK_STATE +uid,value).commit();
    }

    public static int getNetworkState(Context ctx, int uid) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERRENCE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(NETWORK_STATE +uid,DEFAULT);
    }

    public static void clear(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERRENCE_NAME, Context.MODE_PRIVATE);
        pref.edit().clear().commit();
    }
}
