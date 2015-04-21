package com.tieto.systemmanagement.diskmonitor.entity;

import android.graphics.Bitmap;

/**
 * Created by wangbo on 4/21/15.
 */
public class ThumbNailInfo {
    public String mThumbnailPath[];
    public String[] mArrPath;
    public Bitmap[] mThumbnails;

    public ThumbNailInfo(final Integer count) {
        mThumbnailPath = new String[count];
        mArrPath = new String[count];
        mThumbnails = new Bitmap[count];
    }
}
