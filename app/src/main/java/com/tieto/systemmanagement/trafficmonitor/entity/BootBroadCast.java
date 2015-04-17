package com.tieto.systemmanagement.trafficmonitor.entity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tieto.systemmanagement.trafficmonitor.service.CalculateBackGroundTrafficService;

/**
 * Created by jane on 15-4-16.
 */
public class BootBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // start service to calculate the background traffic used
        // by each app when boot
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(context, CalculateBackGroundTrafficService.class);
        serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(serviceIntent);
    }
}
