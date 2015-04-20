package com.tieto.systemmanagement.intercept.service;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

import com.tieto.systemmanagement.intercept.entity.SmsInfo;
import com.tieto.systemmanagement.intercept.entity.SmsInfo.SmsColumn;
import com.tieto.systemmanagement.intercept.util.InterceptHelper;

/**
 * Created by zhaooked on 4/15/15.
 */
public class SmsManagement extends ContentObserver {

    private ContentResolver mResolver;
    public Context mContext;

    public SmsManagement(ContentResolver mResolver, Context context) {
        super(null);
        this.mResolver = mResolver;
        this.mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {

        Log.i("Sms self change ",selfChange+ "") ;
        Cursor mCursor = mResolver.query(Uri.parse("content://sms/inbox"),
                new String[]{SmsColumn.ID, SmsColumn.ADDRESS, SmsColumn.READ, SmsColumn.BODY, SmsColumn.THREAD_ID, SmsColumn.DATE, SmsColumn.PERSON},
                SmsColumn.READ + "=? and " + SmsColumn.TYPE + " = ?",
                new String[]{String.valueOf(SmsInfo.ReadValue.UN_READ), String.valueOf(SmsInfo.TypeValue.IN_BOX)},
                "date desc"
        );
        if (mCursor == null) {
            return;
        } else {
            mCursor.moveToFirst();
            SmsInfo mSmsInfo = new SmsInfo();
            mSmsInfo.setAddress(mCursor.getString(mCursor.getColumnIndex(SmsColumn.ADDRESS)));
            mSmsInfo.setId(mCursor.getInt(mCursor.getColumnIndex(SmsColumn.ID)));
            mSmsInfo.setBody(mCursor.getString(mCursor.getColumnIndex(SmsColumn.BODY)));
            mSmsInfo.setDate(mCursor.getLong(mCursor.getColumnIndex(SmsColumn.DATE)));
            mSmsInfo.setPerson(mCursor.getString(mCursor.getColumnIndex(SmsColumn.PERSON)));
            mSmsInfo.setRead(mCursor.getInt(mCursor.getColumnIndex(SmsColumn.READ)));
            mSmsInfo.setThreadID(mCursor.getInt(mCursor.getColumnIndex(SmsColumn.THREAD_ID)));
            Log.i("Sms change inbox",mSmsInfo.getBody()) ;
            Intent intent = new Intent(mContext, PhoneFilterServer.class);
            intent.putExtra("smsinfo", mSmsInfo) ;
            intent.setAction(InterceptHelper.INTERCEPT_ACTION_MESSAGE);
            mContext.startService(intent);
        }

        mCursor.close();
        mCursor = null;
    }
}
