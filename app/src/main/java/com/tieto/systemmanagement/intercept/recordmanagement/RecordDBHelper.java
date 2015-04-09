package com.tieto.systemmanagement.intercept.recordmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tieto.systemmanagement.intercept.entity.Record;

/**
 * Created by zhaooked on 4/9/15.
 */
public class RecordDBHelper extends SQLiteOpenHelper {

    private static String  DATABASE_NAME = "filter_record.db3";

    // Table name
    private static final String RECORD = "record";
    private static final int DATABASE_VERSION = 1;

    private static final String ID = "id";
    private static final String INTERCEPT_TYPE = "interceptType";
    private static final String MANIFEST_TYPE = "manifestType";
    private static final String FILTRATION_TYPE = "filtrationType";
    private static final String RECORD_CONTENT = "recordContent";



    private static String CREATE_TABLE_SQL = "create table" + RECORD + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            INTERCEPT_TYPE + " INTEGER NOT NULL , " +
            MANIFEST_TYPE + " INTEGER NOT NULL , " +
            FILTRATION_TYPE + " INTEGER NOT NULL ," +
            RECORD_CONTENT + " TEXT NOT NULL" +
            " ) " ;

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
        addItem(getWritableDatabase(),record) ;
    }

    private void addItem(SQLiteDatabase db, Record item) {
        // prepare values
        ContentValues values = new ContentValues();
        values.put(INTERCEPT_TYPE, item.getInterceptType());
        values.put(MANIFEST_TYPE, item.getManifestType());
        values.put(FILTRATION_TYPE, item.getFiltrationType());
        values.put(RECORD_CONTENT, item.getRecordContent());
        // add the row
        db.insert(RECORD, null, values);
    }

}
