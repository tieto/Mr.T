package com.tieto.systemmanagement.intercept.entity;

/**
 * Created by zhaooked on 4/9/15.
 */
public class Record {

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

    private int id ;

    private int interceptType ;
    private int manifestType ;
    private int filtrationType ;
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
