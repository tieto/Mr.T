package com.tieto.systemmanagement.trafficmonitor.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tieto.systemmanagement.trafficmonitor.entity.IptablesForDroidWall;

/**
 * Created by jane on 15-4-4.
 */
public class PackageBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_PACKAGE_REMOVED.equals(intent)) {
            boolean replaced = intent.getBooleanExtra(Intent.EXTRA_REPLACING,false);
            if(!replaced) {
                int uid = intent.getIntExtra(Intent.EXTRA_UID,-123);
                IptablesForDroidWall.applicationRemoved(context, uid);
            }
        }
    }
}
