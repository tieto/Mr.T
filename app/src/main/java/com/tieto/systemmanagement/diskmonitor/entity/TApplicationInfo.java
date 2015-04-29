package com.tieto.systemmanagement.diskmonitor.entity;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by wangbo on 4/21/15.
 */
public class TApplicationInfo {
    public String mAppName = "";
    public String mName = "";
    public String mPath = "";
    public Drawable icon;
    public long   mSize = 0;

    public void prettyPrint() {
        Log.v("App - ",mAppName + mName);
    }
}

