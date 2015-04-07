package com.tieto.systemmanagement.app.model;

/**
 * Created by jinpei on 26/03/15.
 */
public class AppSizeModel {

    /**
     * Package　name.
     */
    private String packageName;

    /**
     * Size of app's cache.
     */
    private long cacheSize;

    /**
     * Size of app's data.
     */
    private long dataSize;

    /**
     * Size of app's program.
     */
    private long programSize;

    public AppSizeModel() {

    }

    public AppSizeModel(String packageName, long cacheSize, long dataSize, long programSize) {
        this.packageName = packageName;
        this.cacheSize = cacheSize;
        this.dataSize = dataSize;
        this.programSize = programSize;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public long getDataSize() {
        return dataSize;
    }

    public long getProgramSize() {
        return programSize;
    }

    public long getTotalSize() {
        return dataSize + programSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    public void setProgramSize(long programSize) {
        this.programSize = programSize;
    }
}
