package com.tieto.systemmanagement.trafficmonitor.entity;

import android.graphics.drawable.Drawable;

import com.tieto.systemmanagement.trafficmonitor.constant.CommonConstant;

/**
 * Created by jane on 15-3-24.
 */
public class AppInfoEntity {
    private int uid;
    private Drawable appIcon;
    private String appName;
    private float appTrafficUsed;
    private float appTrafficUsedBg;
    private int isNetworkAllowed;
    private float appNetSpeeed;
    private float appTrafficSneaked;

    public AppInfoEntity() {
    }

    public AppInfoEntity(String appName, float appTrafficUsed, float appTrafficUsedBg, int isNetworkAllowed, float appNetSpeeed, float appTrafficSneaked) {

        this.appName = appName;
        this.appTrafficUsed = appTrafficUsed;
        this.appTrafficUsedBg = appTrafficUsedBg;
        this.isNetworkAllowed = isNetworkAllowed;
        this.appNetSpeeed = appNetSpeeed;
        this.appTrafficSneaked = appTrafficSneaked;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public float getAppTrafficUsedBg() {
        return appTrafficUsedBg;
    }

    public void setAppTrafficUsedBg(float appTrafficUsedBg) {
        this.appTrafficUsedBg = appTrafficUsedBg;
    }

    public float getAppTrafficUsed() {
        return appTrafficUsed;
    }

    public void setAppTrafficUsed(float appTrafficUsed) {
        this.appTrafficUsed = appTrafficUsed;
    }

    public String getIsNetworkAllowed() {
        String netType = "";
        switch (isNetworkAllowed) {
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

    public void setIsNetworkAllowed(int isNetworkAllowed) {
        this.isNetworkAllowed = isNetworkAllowed;
    }

    public float getAppNetSpeeed() {
        return appNetSpeeed;
    }

    public void setAppNetSpeeed(float appNetSpeeed) {
        this.appNetSpeeed = appNetSpeeed;
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public float getAppTrafficSneaked() {
        return appTrafficSneaked;
    }

    public void setAppTrafficSneaked(float appTrafficSneaked) {
        this.appTrafficSneaked = appTrafficSneaked;
    }
}
