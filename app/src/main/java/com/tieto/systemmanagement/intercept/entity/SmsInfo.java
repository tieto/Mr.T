package com.tieto.systemmanagement.intercept.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaooked on 4/15/15.
 */
public class SmsInfo implements Parcelable {

    public interface SmsColumn{
        String ID = "_id" ;
        String THREAD_ID = "thread_id" ;
        String ADDRESS = "address" ;
        String DATE = "date" ;
        String READ = "read" ;
        String PERSON = "person" ;
        String STATUS = "status" ;
        String TYPE = "type" ;
        String BODY = "body" ;
    }

    public interface TypeValue{
        int IN_BOX = 1 ;
        int SEND_OUT =2 ;
    }

    public interface ReadValue{
        int READ = 1 ;
        int UN_READ = 0 ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private int id  ;

    private int threadID ;

    private String address ;

    private long date ;

    private int read ;

    private String person ;

    private int status ;

    private int type ;

    private String body ;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(threadID);
        dest.writeString(address);
        dest.writeLong(date);
        dest.writeInt(read);
        dest.writeString(person);
        dest.writeInt(status);
        dest.writeInt(type);
        dest.writeString(body);
    }

    public static final Creator<SmsInfo> CREATOR = new Creator<SmsInfo>() {
        @Override
        public SmsInfo createFromParcel(Parcel source) {
            SmsInfo sms = new SmsInfo();
            sms.setId(source.readInt());
            sms.setThreadID(source.readInt());
            sms.setAddress(source.readString());
            sms.setDate(source.readLong());
            sms.setRead(source.readInt());
            sms.setPerson(source.readString());
            sms.setStatus(source.readInt());
            sms.setType(source.readInt());
            sms.setBody(source.readString());
            return sms;
        }

        @Override
        public SmsInfo[] newArray(int size) {
            return new SmsInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


}
