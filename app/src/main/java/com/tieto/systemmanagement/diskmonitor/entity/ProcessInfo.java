package com.tieto.systemmanagement.diskmonitor.entity;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by wangbo on 4/21/15.
 */
public class ProcessInfo {
    public String mAppName = "";
    public String mName = "";
    public String mVersionName = "";
    public String mPath = "";
    public int mVersionCode = 0;
    public Drawable icon;
    public long   mSize = 0;

    public void prettyPrint() {
        Log.v("App - ",mAppName + mName  + mVersionName + + mVersionCode);
    }
}

