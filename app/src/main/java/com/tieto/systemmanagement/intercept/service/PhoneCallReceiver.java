package com.tieto.systemmanagement.intercept.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;


import com.android.internal.telephony.ITelephony;

/**
 * Created by zhaooked on 4/2/15.
 */
public class PhoneCallReceiver extends BroadcastReceiver {

    Context context = null;
    private static final String TAG = "THIRI THE WUT YEE";
    private ITelephony telephonyService;

    private PhoneFilterServer phoneFilterServer ;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
               phoneFilterServer = ((PhoneFilterServer.myBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent2 = new Intent(context, PhoneFilterServer.class);
        intent2.putExtras(intent);
//        context.startService(intent2);

    }
}
