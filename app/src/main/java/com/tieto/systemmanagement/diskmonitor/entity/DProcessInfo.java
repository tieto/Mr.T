package com.tieto.systemmanagement.diskmonitor.entity;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by wangbo on 4/8/15.
 */

 public class DProcessInfo {
    public String appname = "";
    public String pname = "";
    public String versionName = "";
    public int versionCode = 0;
    public String path = "";
    public Drawable icon;

    public void prettyPrint() {
        Log.i("Here", appname + "\t" + pname + "\t" + versionName + "\t" + versionCode + "\t" + path);
    }
}