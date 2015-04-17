package com.tieto.systemmanagement.trafficmonitor.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;

import com.tieto.systemmanagement.trafficmonitor.entity.AppTrafficInfo;
import com.tieto.systemmanagement.trafficmonitor.entity.TrafficStatsWrapper;
import com.tieto.systemmanagement.trafficmonitor.storage.TrafficMonitorPref;

import java.util.List;

/**
 * Created by jane on 15-4-15.
 */
public class ShutDownBroadCast extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        // when shut down & reboot ,
        // save the traffic info into sharePreference
        TrafficStats trafficStats = new TrafficStats();

        //save the total traffic used by all apps since boot
        long totalRxBytes = trafficStats.getTotalRxBytes();
        TrafficMonitorPref.save(context, totalRxBytes,
                TrafficMonitorPref.FLAG_TOTAL, -1);

        //keep the traffic each app used since boot
        List<AppTrafficInfo> nonSystemApps ;
        TrafficStatsWrapper trafficStatsWrapper = new TrafficStatsWrapper(context);
        nonSystemApps = trafficStatsWrapper.getAppTrafficInfoList();
        long totalUidRxBytes = 0;
        if(nonSystemApps != null && nonSystemApps.size() != 0) {
            for (AppTrafficInfo app : nonSystemApps) {
                int uid = app.getmAppWrapper().getUid();
                totalUidRxBytes = trafficStats.getUidRxBytes(uid);
                TrafficMonitorPref.save(context, totalUidRxBytes, TrafficMonitorPref.FLAG_UID_TOTAL, uid);
            }
        }
    }
}
