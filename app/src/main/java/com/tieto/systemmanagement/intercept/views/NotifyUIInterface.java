package com.tieto.systemmanagement.intercept.views;

import com.tieto.systemmanagement.intercept.entity.Record;
import com.tieto.systemmanagement.intercept.recordmanagement.RecordDBHelper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by zhaooked on 4/17/15.
 */
public abstract class NotifyUIInterface implements RecordDBHelper.Notification{

    private Handler handler ;

    private NotifyUIInterface(){

    }

    public NotifyUIInterface(Context context){
        handler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                doNotify((Record)msg.obj);
            }
        };
    }

    @Override
    public void notify(Record record) {
        Message message = handler.obtainMessage(0, record);
        handler.sendMessage(message) ;
    };

    public abstract void doNotify(Record record) ;

}
