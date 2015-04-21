package com.tieto.systemmanagement.intercept.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zhaooked on 4/17/15.
 *
 * Start PhoneFilter server when boot (keep running in background)
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            context.startService(new Intent(context,PhoneFilterServer.class)) ;
        }
    }
}
