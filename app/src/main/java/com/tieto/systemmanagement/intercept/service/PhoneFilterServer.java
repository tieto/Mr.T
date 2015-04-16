package com.tieto.systemmanagement.intercept.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.tieto.systemmanagement.intercept.entity.Record;
import com.tieto.systemmanagement.intercept.entity.SmsInfo;
import com.tieto.systemmanagement.intercept.recordmanagement.RecordDBHelper;
import com.tieto.systemmanagement.intercept.util.InterceptHelper;
import com.tieto.systemmanagement.intercept.util.InterceptHelper.InterceptCallConfiguration;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhaooked on 4/7/15.
 */
public class PhoneFilterServer extends Service {


    private static final String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String TAG = "Phone Filter Server";
    private static final Uri mUri= Uri.parse("content://sms/inbox");

    private boolean mIsIntercept;
    private boolean mInterceptContract;
    private boolean mInterceptAnonymity;
    private boolean mInterceptStrange;

    private boolean mIsMessageIntercept;
    private boolean mMessageInterceptStrange ;

    private Set<String> mPhoneNumberList = new HashSet<String>();
    private Set<String> mBlackList = new HashSet<String>();
    private Set<String> mWhiteList = new HashSet<String>();

    private ITelephony telephonyService;
    private TelephonyManager telephonyManager ;
    private RecordDBHelper mRecordDBHelper;
    private static ContentObserver mContentObserver ;


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

        Log.d(TAG,"called action " + intent.getAction()) ;
        if (intent.getAction().equals(InterceptHelper.INTERCEPT_ACTION_CALL)) {
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
                        String format = getCurrentDate();
                        record.setDate(format);
                        mRecordDBHelper.insert(record);
                    }
                    break;
            }
        } else if(intent.getAction().equals(InterceptHelper.INTERCEPT_ACTION_MESSAGE)){

            SmsInfo smsInfo = intent.getParcelableExtra("smsinfo") ;
            if (isInterceptMessage(smsInfo.getAddress())) {
                deleteSmsAndAddRecord(smsInfo);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private boolean isInterceptMessage(String address) {
        if (mWhiteList.contains(address)) {
            return false;
        }
        if (mBlackList.contains(address)) {
            return true;
        }
        if (mMessageInterceptStrange && !mPhoneNumberList.contains(address)){
            return true ;
        }
        return false;
    }

    private void deleteSmsAndAddRecord(SmsInfo smsInfo) {
        //delete sms
        getContentResolver().delete(mUri, "_id=?", new String[]{String.valueOf(smsInfo.getId())});
        //insert sms record
        Record record = new Record();
        record.setInterceptType(Record.InterceptType.INCOMING_MESSAGE);
        record.setRecordContent(smsInfo.getBody());
        record.setManifestType(Record.ManifestType.RECORD_LIST);
        record.setReMark(smsInfo.getPerson());
        record.setDate(DateLongToString(smsInfo.getDate()));
        mRecordDBHelper.insert(record);
    }

    private String DateLongToString(Long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(time) ;
    }

    private String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(c.getTime());
    }

    /**
     * Filter conditions
     * @param incomingPhoneNumber
     * @return
     */
    private boolean isIntercept(String incomingPhoneNumber) {

        boolean intercept = false;
        if(!mIsIntercept){
            return intercept ;
        }
        if (mInterceptAnonymity && incomingPhoneNumber == null) {
            return true ;
        }
        if(mBlackList.contains(incomingPhoneNumber)){
            return true ;
        }
        if(mWhiteList.contains(incomingPhoneNumber)){
            return false ;
        }
        if (mInterceptStrange){
            intercept = !mPhoneNumberList.contains(incomingPhoneNumber);
        }
        if (mInterceptContract) {
            intercept = mPhoneNumberList.contains(incomingPhoneNumber);
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
        SharedPreferences sharedPreferences = getSharedPreferences(InterceptHelper.INTERCEPT_CONFIGURATION, MODE_PRIVATE);
        mIsIntercept = sharedPreferences.getBoolean(InterceptCallConfiguration.ENABLE_CALL_INTERCEPT,InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT);
        mInterceptContract = sharedPreferences.getBoolean(InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_CONTRACT,InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT_CONTRACT) ;
        mInterceptAnonymity = sharedPreferences.getBoolean(InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_ANONYMITY,InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT_ANONYMITY) ;
        mInterceptStrange = sharedPreferences.getBoolean(InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_STRANGE,InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT_STRANGE) ;
        mIsMessageIntercept = sharedPreferences.getBoolean(InterceptHelper.InterceptMessageConfiguration.ENABLE_MESSAGE_INTERCEPT,InterceptHelper.InterceptMessageConfiguration.DEFAULT_ENABLE_MESSAGE_INTERCEPT) ;
        mMessageInterceptStrange = sharedPreferences.getBoolean(InterceptHelper.InterceptMessageConfiguration.ENABLE_MESSAGE_INTERCEPT_STRANGE,InterceptHelper.InterceptMessageConfiguration.DEFAULT_ENABLE_MESSAGE_INTERCEPT_STRANGE) ;
        //Get all contract number
        mPhoneNumberList.clear();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                mPhoneNumberList.add(cursor.getString(cursor.getColumnIndex(NUMBER)));
            } catch (Exception e) {
                //
            }
        }

        if(mRecordDBHelper == null){
            mRecordDBHelper = new RecordDBHelper(this) ;
        }
        //Prepare list
        mBlackList.clear();
        List<Record> blackRecords= mRecordDBHelper.queryCallLListRecords(Record.ManifestType.BLACK_LIST);
        for(Record record : blackRecords){
            mBlackList.add(record.getRecordContent()) ;
        }
        mWhiteList.clear();
        List<Record> whiteRecords= mRecordDBHelper.queryCallLListRecords(Record.ManifestType.WHITE_LIST);
        for(Record record : blackRecords){
            mWhiteList.add(record.getRecordContent()) ;
        }

        //Prepare phone service
        if(telephonyManager == null){
            telephonyManager = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        }

        if(mIsMessageIntercept) {
            registerSmsDataBase();
        }else{
            unRegisterSmsDataBase() ;
        }
    }

    private void registerSmsDataBase() {
        Log.i(TAG,"register sms contentObserver") ;
        ContentResolver resolver = getContentResolver();
        if(mContentObserver == null) {
            mContentObserver = new SmsManagement(resolver, this);
        }
        resolver.registerContentObserver(Uri.parse("content://sms"), true, mContentObserver);
    }

    private void unRegisterSmsDataBase(){
        Log.i(TAG,"unRegister sms contentObserver") ;
        if(mContentObserver!=null) {
            getContentResolver().unregisterContentObserver(mContentObserver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterSmsDataBase();
    }
}


