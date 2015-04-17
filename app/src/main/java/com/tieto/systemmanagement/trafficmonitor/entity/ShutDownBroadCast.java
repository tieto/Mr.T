package com.tieto.systemmanagement.trafficmonitor.entity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;

import com.tieto.systemmanagement.trafficmonitor.storage.TrafficStasticPreferrence;

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
        TrafficStasticPreferrence.save(context, totalRxBytes,
                TrafficStasticPreferrence.FLAG_TOTAL, -1);

        //keep the traffic each app used since boot
        List<AppInfoEntity> nonSystemApps ;
        TrafficStatsWrapper trafficStatsWrapper = new TrafficStatsWrapper(context);
        nonSystemApps = trafficStatsWrapper.getmTrafficStaticAppInfoLists();
        long totalUidRxBytes = 0;
        if(nonSystemApps != null && nonSystemApps.size() != 0) {
            for (AppInfoEntity app : nonSystemApps) {
                int uid = app.getUid();
                totalUidRxBytes = trafficStats.getUidRxBytes(uid);
                TrafficStasticPreferrence.save(context, totalUidRxBytes, TrafficStasticPreferrence.FLAG_UID_TOTAL, uid);
            }
        }
    }
}
