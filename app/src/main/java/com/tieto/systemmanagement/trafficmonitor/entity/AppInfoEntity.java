package com.tieto.systemmanagement.trafficmonitor.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by jane on 15-3-24.
 */
public class AppInfoEntity {
    private int mUid;
    private Drawable mAppIcon;
    private String mAppName;
    private float mAppTrafficUsed;
    private float mAppTrafficUsedBg;
    private int mIsNetworkAllowed;
    private float mAppNetSpeeed;
    private float mAppTrafficSneaked;

    public AppInfoEntity() {
    }

    public AppInfoEntity(String appName, float appTrafficUsed, float appTrafficUsedBg, int isNetworkAllowed, float appNetSpeeed, float appTrafficSneaked) {

        this.mAppName = appName;
        this.mAppTrafficUsed = appTrafficUsed;
        this.mAppTrafficUsedBg = appTrafficUsedBg;
        this.mIsNetworkAllowed = isNetworkAllowed;
        this.mAppNetSpeeed = appNetSpeeed;
        this.mAppTrafficSneaked = appTrafficSneaked;
    }

    public Drawable getmAppIcon() {
        return mAppIcon;
    }

    public void setmAppIcon(Drawable mAppIcon) {
        this.mAppIcon = mAppIcon;
    }

    public String getmAppName() {
        return mAppName;
    }

    public void setmAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public float getmAppTrafficUsedBg() {
        return mAppTrafficUsedBg;
    }

    public void setmAppTrafficUsedBg(float mAppTrafficUsedBg) {
        this.mAppTrafficUsedBg = mAppTrafficUsedBg;
    }

    public float getmAppTrafficUsed() {
        return mAppTrafficUsed;
    }

    public void setmAppTrafficUsed(float mAppTrafficUsed) {
        this.mAppTrafficUsed = mAppTrafficUsed;
    }

    public String getmIsNetworkAllowed() {
        String netType = "";
        switch (mIsNetworkAllowed) {
            /*case CommonConstant.SNEAKING_PROHIBITED:
                break;*/
            case 0:
                netType = "禁止偷跑";
                break;
            case 1:
                netType = "禁止联网";
                break;
            case 2:
                netType = "允许网络";
                break;
            case 3:
                netType = "仅wifi连接";
                break;

        }
        return netType;
    }

    public void setmIsNetworkAllowed(int mIsNetworkAllowed) {
        this.mIsNetworkAllowed = mIsNetworkAllowed;
    }

    public float getmAppNetSpeeed() {
        return mAppNetSpeeed;
    }

    public void setmAppNetSpeeed(float mAppNetSpeeed) {
        this.mAppNetSpeeed = mAppNetSpeeed;
    }


    public int getUid() {
        return mUid;
    }

    public void setUid(int uid) {
        this.mUid = uid;
    }

    public float getmAppTrafficSneaked() {
        return mAppTrafficSneaked;
    }

    public void setmAppTrafficSneaked(float mAppTrafficSneaked) {
        this.mAppTrafficSneaked = mAppTrafficSneaked;
    }
}
