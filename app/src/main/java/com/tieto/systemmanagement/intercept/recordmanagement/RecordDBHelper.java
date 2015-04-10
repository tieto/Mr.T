package com.tieto.systemmanagement.intercept.recordmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tieto.systemmanagement.intercept.entity.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaooked on 4/9/15.
 */
public class RecordDBHelper extends SQLiteOpenHelper {

    private static String  DATABASE_NAME = "filter_record.db3";

    // Table name
    private static final String RECORD = "record";

    //Table version
    private static final int DATABASE_VERSION = 1;

    //id
    private static final String ID = "id";

    //(Phone/Message) intercept
    private static final String INTERCEPT_TYPE = "interceptType";

    //The (Black/White) list, record
    private static final String MANIFEST_TYPE = "manifestType";

    //Phone number, Message keyword
    private static final String FILTRATION_TYPE = "filtrationType";

    //The content of the record
    private static final String RECORD_CONTENT = "recordContent";

    //The date of the record
    private static final String DATE = "date" ;

    //Table sql
    private static String CREATE_TABLE_SQL = "create table " + RECORD + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            INTERCEPT_TYPE + " INTEGER NOT NULL , " +
            MANIFEST_TYPE + " INTEGER NOT NULL , " +
            FILTRATION_TYPE + " INTEGER NOT NULL ," +
            RECORD_CONTENT + " TEXT NOT NULL ," +
            DATE + " TEXT NOT NULL " +
            " ) " ;

    private static List<Notification> listener = new ArrayList<Notification>() ;

    public RecordDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + RECORD);

        onCreate(db);
    }

    public void insert(Record record){
        addItem(getWritableDatabase(), record) ;
    }

    public List<Record> queryPhoneInterceptRecord(){
        return queryRecords(Record.InterceptType.INCOMING_PHONE,Record.ManifestType.RECORD_LIST,Record.FiltrationType.NUMBER) ;
    }

    public void queryAllRecord(){
        Cursor query = getReadableDatabase().query(RECORD, null, null, null, null, null, null);
        if(query.moveToNext()) {
           String s =  query.getString(query.getColumnIndex(RECORD_CONTENT));
        }
    }



    private List<Record> queryRecords(int interceptType,int manifestType,int filtrationType){
        Cursor query = getReadableDatabase().query(RECORD,
                                                   new String[]{ID, RECORD_CONTENT, DATE},
                                                   INTERCEPT_TYPE + " = ? and " + MANIFEST_TYPE + " = ? and " + FILTRATION_TYPE + " = ? ",
                                                   new String[]{String.valueOf(interceptType), String.valueOf(manifestType),String.valueOf(filtrationType)},
                                                   null,
                                                   null,
                                                   null);
        List<Record> records = new ArrayList<Record>() ;
        while(query.moveToNext()){
            Record record = new Record() ;
            record.setDate( query.getString(query.getColumnIndex(DATE)));
            record.setRecordContent(query.getString(query.getColumnIndex(RECORD_CONTENT)));
            record.setId(query.getInt(query.getColumnIndex(ID)));
            records.add(record) ;
        }
        query.close();
        return records ;
    }

    private void addItem(SQLiteDatabase db, Record item) {
        // prepare values
        ContentValues values = new ContentValues();
        values.put(INTERCEPT_TYPE, item.getInterceptType());
        values.put(MANIFEST_TYPE, item.getManifestType());
        values.put(FILTRATION_TYPE, item.getFiltrationType());
        values.put(RECORD_CONTENT, item.getRecordContent());
        values.put(DATE, item.getDate());
        // add the row
        db.insert(RECORD, null, values);
        // Notification
        for(Notification notify : listener){
            notify.notify(item);
        }
    }

    public void registerNotification(Notification notify){
        listener.add(notify) ;
    }

    public void unRegisterNotification(Notification notify){
        listener.remove(notify) ;
    }

    public void clear() {
        listener.clear();
    }


    public interface Notification {
        void notify(Record record) ;
    }

}
