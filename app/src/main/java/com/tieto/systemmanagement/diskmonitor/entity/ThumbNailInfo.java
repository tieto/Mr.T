package com.tieto.systemmanagement.diskmonitor.entity;

import android.graphics.Bitmap;

/**
 * Created by wangbo on 4/21/15.
 */
public class ThumbNailInfo {
    public String mItemlPath[];
    public String[] mArrPath;
    public Bitmap[] mItem;

    public ThumbNailInfo(final Integer count) {
        mItemlPath = new String[count];
        mArrPath = new String[count];
        mItem = new Bitmap[count];
    }
}
