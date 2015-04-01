package com.tieto.systemmanagement.trafficmonitor.storage;
/**
 * record each app's traffic usage and all the traffic usage info.
 */
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jane on 15-3-25.
 */
public class AppTrafficUsedInfoPref {

    //记录的信息：
    // 是否是第一次使用该app
    //记录所有APP当前累计使用的流量 N
    //记录每个APP当前累计使用的流量 X
    //网速测算：
    //记录当月套餐设定的总值 M （月底清零）

    private static final String PREFERRENCE_NAME = "appTrafficUsageInfo";

    private static final String KEY_TOTAL = "key_total";
    private static final String KEY_ONEBOOT = "key_oneboot";
    private static final float DEFAULT = 0;

    public static void save(Context context,float value) {
        SharedPreferences pref = context.getSharedPreferences(PREFERRENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        float total = pref.getFloat(KEY_TOTAL,DEFAULT);
        editor.putFloat(KEY_TOTAL,total+value);
        editor.putFloat(KEY_ONEBOOT,value);
        editor.commit();

    }

    public static float get(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERRENCE_NAME, Context.MODE_PRIVATE);
        return pref.getFloat(KEY_TOTAL,DEFAULT);
    }

}
