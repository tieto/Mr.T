package com.tieto.systemmanagement.intercept.recordmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tieto.systemmanagement.intercept.entity.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaooked on 4/9/15.
 */
public class RecordDBHelper extends SQLiteOpenHelper {

    private static String TAG = "RecordDBHelper" ;

    private static String  DATABASE_NAME = "filter_record.db3";

    /**
     * Table name
     */
    private static final String RECORD = "record";

    /**
     * Table version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * id
     */
    private static final String ID = "id";

    /**
     * (Phone/Message) intercept
     */
    public static final String INTERCEPT_TYPE = "interceptType";

    /**
     * The (Black/White) list, record
     */
    public static final String MANIFEST_TYPE = "manifestType";

    /**
     * Phone number, Message keyword
     */
    public static final String FILTRATION_TYPE = "filtrationType";

    /**
     * The content of the record
     */
    public static final String RECORD_CONTENT = "recordContent";

    /**
     * The date of the record
     */
    public static final String DATE = "date" ;

    public static final String REMARK = "remark"  ;

    /**
     * Table sql
     */
    private static String CREATE_TABLE_SQL = "create table " + RECORD + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            INTERCEPT_TYPE + " INTEGER NOT NULL , " +
            MANIFEST_TYPE + " INTEGER NOT NULL , " +
            FILTRATION_TYPE + " INTEGER ," +
            RECORD_CONTENT + " TEXT NOT NULL ," +
            DATE + " TEXT NOT NULL ," +
            REMARK + " TEXT " +
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
        return queryRecords(new String[]{INTERCEPT_TYPE,MANIFEST_TYPE},new String[]{String.valueOf(Record.InterceptType.INCOMING_PHONE),String.valueOf(Record.ManifestType.RECORD_LIST)}) ;
    }

    public List<Record> queryMessageInterceptRecord(){
        return queryRecords(new String[]{INTERCEPT_TYPE,MANIFEST_TYPE},new String[]{String.valueOf(Record.InterceptType.INCOMING_MESSAGE),String.valueOf(Record.ManifestType.RECORD_LIST)}) ;
    }

    public List<Record> queryCallLListRecords(int manifestType){
        return queryRecords(new String[]{INTERCEPT_TYPE,MANIFEST_TYPE},new String[] {String.valueOf(Record.InterceptType.INCOMING_PHONE),String.valueOf(manifestType)}) ;
    }

    public List<Record> queryMessageListRecords(int manifestType){
        return queryRecords(new String[]{INTERCEPT_TYPE,MANIFEST_TYPE},new String[] {String.valueOf(Record.InterceptType.INCOMING_PHONE),String.valueOf(manifestType)}) ;
    }


    public void updateRecord(Record record){
        ContentValues values = new ContentValues();
        values.put(INTERCEPT_TYPE, record.getInterceptType());
        values.put(MANIFEST_TYPE, record.getManifestType());
        values.put(FILTRATION_TYPE, record.getFiltrationType());
        values.put(RECORD_CONTENT, record.getRecordContent());
        values.put(DATE, record.getDate());
        SQLiteDatabase db = getWritableDatabase();
        db.update(RECORD, values, ID + " = ?", new String[]{String.valueOf(record.getId())}) ;
        Log.i(TAG,"update Record ") ;
        db.close();
    }

    public void deleteAllPhoneRecords(){
        getWritableDatabase().delete(RECORD,INTERCEPT_TYPE + " = ? and " + MANIFEST_TYPE + " = ?",new String[]{String.valueOf(Record.InterceptType.INCOMING_PHONE),String.valueOf(Record.ManifestType.RECORD_LIST)}) ;
        notifyUI(null) ;
    }

    public void deleteAllMessageRecords(){
        getWritableDatabase().delete(RECORD,INTERCEPT_TYPE + " = ? and " + MANIFEST_TYPE + " = ?",new String[]{String.valueOf(Record.InterceptType.INCOMING_MESSAGE),String.valueOf(Record.ManifestType.RECORD_LIST)}) ;
        notifyUI(null) ;
    }

    public void deleteRecords(Record record) {
        getWritableDatabase().delete(RECORD,ID + " = ?" ,new String[]{String.valueOf(record.getId())}) ;
    }


    /**
     * Query record by column
     *
     * key   (interceptType,manifestType,filtrationType,recordContent)
     *
     * value (@See Record.InterceptType,Record.ManifestType,Record.FiltrationType)
     *
     * @param keys
     * @param values
     * @return
     */
    public List<Record> queryRecords(String[] keys,String[] values){

        Cursor query ;
        if(keys ==null && values ==null){
            query = getReadableDatabase().query(RECORD, null, null, null, null, null, null);
        }else{
            if(keys.length != values.length) {
                throw new RuntimeException("The keys and values length must be the same");
            }
            StringBuilder selectKeys = new StringBuilder();
            for(int i =0 ;i < keys.length ;i++){
                selectKeys.append(keys[i]).append(" = ?").append(" and ") ;
            }
            query = getReadableDatabase().query(RECORD,
                    new String[]{ID,REMARK, RECORD_CONTENT, DATE},
                    selectKeys.substring(0,selectKeys.lastIndexOf("and")-1),
                    values,
                    null,
                    null,
                    null);
        }
        List<Record> records = new ArrayList<Record>() ;
        while(query.moveToNext()){
            Record record = new Record() ;
            record.setDate( query.getString(query.getColumnIndex(DATE)));
            record.setRecordContent(query.getString(query.getColumnIndex(RECORD_CONTENT)));
            record.setId(query.getInt(query.getColumnIndex(ID)));
            record.setReMark(query.getString(query.getColumnIndex(REMARK)));
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
        values.put(REMARK,item.getReMark()) ;
        // add the row
        db.insert(RECORD, null, values);
        // Notification
        notifyUI(item);
    }

    private void notifyUI(Record item) {
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
