package com.tieto.systemmanagement.intercept.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaooked on 4/9/15.
 */
public class Record implements Parcelable {

    public interface InterceptType{
        int INCOMING_PHONE = 0 ;
        int INCOMING_MESSAGE = 1 ;
    }

    public interface ManifestType{
        int BLACK_LIST = 0 ;
        int WHITE_LIST = 1 ;
        int RECORD_LIST = 2 ;
    }
    public interface FiltrationType{
        int NUMBER = 0 ;
        int CONTENT = 1 ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(manifestType);
        dest.writeInt(filtrationType);
        dest.writeInt(interceptType);
        dest.writeString(recordContent);
        dest.writeString(date);
        dest.writeString(reMark);
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel source) {
            Record record = new Record();
            record.setId(source.readInt());
            record.setManifestType(source.readInt());
            record.setFiltrationType(source.readInt());
            record.setInterceptType(source.readInt());
            record.setRecordContent(source.readString());
            record.setDate(source.readString());
            record.setReMark(source.readString());
            return record;
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    private int id ;

    private int interceptType ;
    private int manifestType ;
    private int filtrationType ;
    private String reMark ;

    public String getReMark() {
        return reMark;
    }

    public void setReMark(String reMark) {
        this.reMark = reMark;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date ;

    public int getInterceptType() {
        return interceptType;
    }

    public void setInterceptType(int interceptType) {
        this.interceptType = interceptType;
    }

    public int getManifestType() {
        return manifestType;
    }

    public void setManifestType(int manifestType) {
        this.manifestType = manifestType;
    }

    public int getFiltrationType() {
        return filtrationType;
    }

    public void setFiltrationType(int filtrationType) {
        this.filtrationType = filtrationType;
    }

    public String getRecordContent() {
        return recordContent;
    }

    public void setRecordContent(String recordContent) {
        this.recordContent = recordContent;
    }

    private String recordContent ;

}
