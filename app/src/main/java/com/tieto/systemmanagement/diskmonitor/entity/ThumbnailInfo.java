package com.tieto.systemmanagement.diskmonitor.entity;

import android.graphics.Bitmap;

import com.tieto.systemmanagement.diskmonitor.utils.DebugToast;

/**
 * Created by wangbo on 4/21/15.
 */
public class ThumbnailInfo {
    public String mItemName;
    public String mItemPath;
    public Bitmap mItemIcon;

    public void prettyPrint() {
       DebugToast.debugToast("",mItemPath);
    }
}
