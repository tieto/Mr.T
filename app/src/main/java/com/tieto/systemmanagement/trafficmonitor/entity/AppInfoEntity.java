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
    private long mAppTrafficUsed;
    //后台使用的流量
    private long mAppTrafficUsedBg;
    //防火墙设置类型
    private int firewallType;
    //app网速
    private float mAppNetSpeeed;
    //后台偷跑流量
    private long mAppTrafficSneaked;

    public AppInfoEntity() {
    }

    public AppInfoEntity(String appName, long appTrafficUsed, long appTrafficUsedBg, int isNetworkAllowed, long appNetSpeeed, long appTrafficSneaked) {

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

    public long getmAppTrafficUsedBg() {
        return mAppTrafficUsedBg;
    }

    public void setmAppTrafficUsedBg(long mAppTrafficUsedBg) {
        this.mAppTrafficUsedBg = mAppTrafficUsedBg;
    }

    public long getmAppTrafficUsed() {
        return mAppTrafficUsed;
    }

    public void setmAppTrafficUsed(long mAppTrafficUsed) {
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

    public long getmAppTrafficSneaked() {
        return mAppTrafficSneaked;
    }

    public void setmAppTrafficSneaked(long mAppTrafficSneaked) {
        this.mAppTrafficSneaked = mAppTrafficSneaked;
    }
}
