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
import com.tieto.systemmanagement.intercept.entity.Record;
import com.tieto.systemmanagement.intercept.recordmanagement.RecordDBHelper;
import com.tieto.systemmanagement.intercept.util.InterceptConfiguration;
import com.tieto.systemmanagement.intercept.util.InterceptConfiguration.InterceptCallConfiguration;
import com.tieto.systemmanagement.intercept.views.CallInterceptConfigActivity;

import java.lang.reflect.Method;
import java.util.Date;
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
    private RecordDBHelper recordDBHelper ;


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        initFilter();
    }

    public class myBinder extends Binder {

        public PhoneFilterServer getService() {
            return PhoneFilterServer.this;
        }
    }

    private final IBinder mBinder = new myBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(InterceptConfiguration.INTERCEPT_ACTION)) {
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
                        Record record = new Record();
                        record.setRecordContent(incomingNumber);
                        record.setInterceptType(Record.InterceptType.INCOMING_PHONE);
                        record.setFiltrationType(Record.FiltrationType.NUMBER);
                        record.setManifestType(Record.ManifestType.RECORD_LIST);
                        record.setDate(new Date().toString());
                        recordDBHelper.insert(record);
                    }
                    break;
            }
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

    public void reInit(){
        initFilter();
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

        recordDBHelper = new RecordDBHelper(this) ;
    }
}


