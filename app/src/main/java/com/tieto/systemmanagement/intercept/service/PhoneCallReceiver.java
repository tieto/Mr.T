package com.tieto.systemmanagement.intercept.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tieto.systemmanagement.intercept.util.InterceptConfiguration;

/**
 * Created by zhaooked on 4/2/15.
 */
public class PhoneCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // If this action is not out going call then it's in coming call status change .
        if (!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Intent intent2 = new Intent(context, PhoneFilterServer.class);
            intent2.putExtras(intent);
            intent2.setAction(InterceptConfiguration.INTERCEPT_ACTION);
            context.startService(intent2);
        }
    }
}
