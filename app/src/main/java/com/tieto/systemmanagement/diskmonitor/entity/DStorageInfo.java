package com.tieto.systemmanagement.diskmonitor.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by wangbo on 4/3/15.
 */
public class DStorageInfo {

    private Drawable icon;
    private String   title;
    private String   total;


    public Drawable getIcon() { return icon;}
    public String   getTitle() {return title;}
    public String getTotal() {return  total;}

    public void setIcon(Drawable spaceIcon) {
        icon = spaceIcon;
    }

    public void setTitle(String spaceTitle) {
        title = spaceTitle;
    }

    public void setTotal(String spaceTotal) {
        total = spaceTotal;
    }

}
