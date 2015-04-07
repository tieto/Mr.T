package com.tieto.systemmanagement.intercept.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhaooked on 4/7/15.
 */
public class PhoneFilterServer extends Service {


    private static String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER ;
    private static String TAG = "Phone Filter" ;

    private boolean intercept_Contract = false ;

    private boolean intercept_Anonymity = true ;

    private Set<String> phoneNumber = new HashSet<String>() ;

    private ITelephony telephonyService;


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        initFilter() ;
    }



    public void get(){

        String tempInputNumber = "18382037880" ;
        String Number = ContactsContract.CommonDataKinds.Phone.NUMBER ;

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, "replace("+Number+",' ','') = ?", new String[]{tempInputNumber}, null);

        while(cursor.moveToNext()){
            String number = cursor.getString(cursor.getColumnIndex(Number));
            Log.e("Phone Number :", number) ;
        }
    }

    public class myBinder extends Binder{

        PhoneFilterServer getService(){
            return PhoneFilterServer.this ;
        }
    }

    private final IBinder mBinder = new myBinder() ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent intent1 = intent ;

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

        } else {

            TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);

            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String incoming_number = intent.getStringExtra("incoming_number");
                    Log.i(TAG, "RINGING :" + incoming_number);
                    Log.e(TAG, "Telephony Manager:" + tm.getCallState());

                    if(isIntercept(incoming_number)) {

                        try {
                            if(telephonyService == null) {
                                Class c = Class.forName(tm.getClass().getName());
                                Method m = c.getDeclaredMethod("getITelephony");
                                m.setAccessible(true);
                                telephonyService = (ITelephony) m.invoke(tm);
                            }

                            telephonyService.endCall();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

            Log.v(TAG, "Receving....");

        }

        return super.onStartCommand(intent, flags, startId);
    }

    public boolean isIntercept(String phoneNumber){

        boolean intercept = false ;
        if(intercept_Anonymity){
            intercept = !phoneNumber.contains(phoneNumber) ;
        }
        if(intercept_Contract){
            intercept = phoneNumber.contains(phoneNumber) ;
        }

        return intercept ;
    }

    private void initFilter(){
        //Get all contract number
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (cursor.moveToNext()){
            try {
                phoneNumber.add(cursor.getString(cursor.getColumnIndex(NUMBER)));
            }catch (Exception e){
                //
            }
        }
    }
}


