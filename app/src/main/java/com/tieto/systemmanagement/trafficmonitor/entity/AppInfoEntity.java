package com.tieto.systemmanagement.trafficmonitor.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by jane on 15-3-24.
 */
public class AppInfoEntity {
    private int mUid;
    private Drawable mAppIcon;
    //app name
    private String mAppName;
    //app在本月使用的流量
    private float mAppTrafficUsed;
    //后台使用的流量
    private float mAppTrafficUsedBg;
    //防火墙设置类型
    private int firewallType;
    //app网速
    private float mAppNetSpeeed;
    //后台偷跑流量
    private float mAppTrafficSneaked;

    public AppInfoEntity() {
    }

    public AppInfoEntity(String appName, float appTrafficUsed, float appTrafficUsedBg, int isNetworkAllowed, float appNetSpeeed, float appTrafficSneaked) {

        this.mAppName = appName;
        this.mAppTrafficUsed = appTrafficUsed;
        this.mAppTrafficUsedBg = appTrafficUsedBg;
        this.firewallType = isNetworkAllowed;
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

    public String getFirewallType() {
        String netType = "";
        switch (firewallType) {
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

    public void setFirewallType(int firewallType) {
        this.firewallType = firewallType;
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
