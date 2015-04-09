package com.tieto.systemmanagement.intercept.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.tieto.systemmanagement.intercept.util.InterceptConfiguration;
import com.tieto.systemmanagement.intercept.util.InterceptConfiguration.InterceptCallConfiguration;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhaooked on 4/7/15.
 */
public class PhoneFilterServer extends Service {


    private static String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static String TAG = "Phone Filter Server";

    private boolean isIntercept ;
    private boolean interceptContract ;
    private boolean interceptAnonymity ;

    private Set<String> phoneNumberList = new HashSet<String>();

    private ITelephony telephonyService;
    private TelephonyManager telephonyManager ;


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        initFilter();
    }


    public void get() {

        String tempInputNumber = "18382037880";
        String Number = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "replace(" + Number + ",' ','') = ?", new String[]{tempInputNumber}, null);

        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndex(Number));
            Log.e("Phone Number :", number);
        }
    }

    public class myBinder extends Binder {

        PhoneFilterServer getService() {
            return PhoneFilterServer.this;
        }
    }

    private final IBinder mBinder = new myBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "Telephony Manager:" + telephonyManager.getCallState());
        switch (telephonyManager.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:
                String incomingNumber = intent.getStringExtra("incoming_number");
                Log.i(TAG, "RINGING :" + incomingNumber);
                if (isIntercept(incomingNumber)) {
                    try {
                        if (telephonyService == null) {
                            Class c = Class.forName(telephonyManager.getClass().getName());
                            Method m = c.getDeclaredMethod("getITelephony");
                            m.setAccessible(true);
                            telephonyService = (ITelephony) m.invoke(telephonyManager);
                        }
                        telephonyService.endCall();

                    } catch (Exception e) {
                        //
                    }
                }
             break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Filter conditions
     * @param incomingPhoneNumber
     * @return
     */
    private boolean isIntercept(String incomingPhoneNumber) {

        boolean intercept = false;
        if(!isIntercept){
            return intercept ;
        }
        if (interceptAnonymity) {
            intercept = !phoneNumberList.contains(incomingPhoneNumber);
        }
        if (interceptContract) {
            intercept = phoneNumberList.contains(incomingPhoneNumber);
        }
        return intercept;
    }

    /**
     * Init Intercept Filter
     */
    private void initFilter() {

        //Prepare intercept condition
        SharedPreferences sharedPreferences = getSharedPreferences(InterceptConfiguration.INTERCEPT_CONFIGURATION, MODE_PRIVATE);
        isIntercept = sharedPreferences.getBoolean(InterceptCallConfiguration.ENABLE_CALL_INTERCEPT,InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT);
        interceptContract = sharedPreferences.getBoolean(InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_CONTRACT,InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT_CONTRACT) ;
        interceptAnonymity = sharedPreferences.getBoolean(InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_ANONYMITY,InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT_ANONYMITY) ;

        //Get all contract number
        phoneNumberList.clear();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                phoneNumberList.add(cursor.getString(cursor.getColumnIndex(NUMBER)));
            } catch (Exception e) {
                //
            }
        }

        //Prepare phone service
        if(telephonyManager == null){
            telephonyManager = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        }
    }
}


