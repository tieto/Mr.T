package com.tieto.systemmanagement.trafficmonitor.storage;
/**
 * record each app's traffic usage and all the traffic usage info.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by jane on 15-3-25.
 */
public class TrafficStasticPreferrence {

    //keep all the traffic stastic info

    private static final String PREFERENCE_NAME = "trafficStaticPreference";
    //key for the total traffic used in the month
    private static final String KEY_TOTAL = "keyTotal";
    //key for the total traffic used by the specified uid app in the month
    private static final String KEY_UID_TOTAL = "keyUidTotal";
    //key for traffic used in background by the app (uid)
    private static final String KEY_UID_BACKGROUND = "keyUidBackground";
    //key for traffic used in background
    private static final String KEY_TOTAL_BACKGROUND = "keyTotalBackground";
    //the default value when no value is saved
    private static final long DEFAULT = 0;
    //flag for save the value for special app
    public static final int FLAG_UID_TOTAL = 4;
    // flag for save all traffic used in background
    private static final int FLAG_TOTAL_BACKGROUND = 3;
    //flag for represent the value as all apps
    public static final int FLAG_TOTAL = 2;
    //flag for the traffic used in background
    public static final int FLAG_UID_BACKGROUND = 1;



    public static void save(Context context,long value,int flag,int uid) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        switch (flag) {
            case FLAG_UID_BACKGROUND:
                long uidTotalBackground = pref.getLong(uid+KEY_UID_BACKGROUND,DEFAULT);
                editor.putLong(uid+KEY_UID_BACKGROUND,(uidTotalBackground+value));
                break;
            case FLAG_TOTAL:
                long total = pref.getLong(KEY_TOTAL,DEFAULT);
                editor.putLong(KEY_TOTAL, (total + value));
                break;
            case FLAG_TOTAL_BACKGROUND:
                break;
            case FLAG_UID_TOTAL:
                long uidTotal = pref.getLong(uid+KEY_UID_TOTAL,DEFAULT);
                editor.putLong(uid+KEY_UID_TOTAL,(uidTotal+value));
                break;
        }
        editor.commit();
    }

    public static long get(Context context,int flag,int uid) {
        long value = 0;
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        switch (flag) {
            case FLAG_UID_BACKGROUND:
                value = pref.getLong(uid+KEY_UID_BACKGROUND,DEFAULT);
                break;
            case FLAG_TOTAL:
                value =  pref.getLong(KEY_TOTAL, DEFAULT);
                break;
            case FLAG_TOTAL_BACKGROUND:
                break;
            case FLAG_UID_TOTAL:
                value = pref.getLong(uid+KEY_UID_TOTAL,DEFAULT);
                break;
        }

        return value;
    }

}
