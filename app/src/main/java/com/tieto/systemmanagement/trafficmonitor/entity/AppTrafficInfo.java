package com.tieto.systemmanagement.trafficmonitor.entity;

import android.graphics.drawable.Drawable;

import com.tieto.systemmanagement.authority.entity.AppWrapper;

/**
 * Created by jane on 15-3-24.
 */
public class AppTrafficInfo {
    // app wrapper include (uid,name,icon)
    private AppWrapper mAppWrapper;

    // app在本月使用的流量
    private long mUsdTrafficInMonth;
    // 当月后台使用的流量
    private long mBgUsedTrafficInMonth;
    // 偷跑的流量
    private long mSneakedTrafficInMonth;
    // 防火墙设置类型
    private int mFirewallType;
    // app网速
    private long mNetworkSpeed;

    public AppTrafficInfo() {
    }

    public AppTrafficInfo(AppWrapper mAppWrapper, long mUsdTrafficInMonth,
                          long mBgUsedTrafficInMonth, long mSneakedTrafficInMonth,
                          int mFirewallType, long mNetworkSpeed) {
        this.mAppWrapper = mAppWrapper;
        this.mUsdTrafficInMonth = mUsdTrafficInMonth;
        this.mBgUsedTrafficInMonth = mBgUsedTrafficInMonth;
        this.mSneakedTrafficInMonth = mSneakedTrafficInMonth;
        this.mFirewallType = mFirewallType;
        this.mNetworkSpeed = mNetworkSpeed;
    }

    public AppWrapper getmAppWrapper() {
        return mAppWrapper;
    }

    public void setmAppWrapper(AppWrapper mAppWrapper) {
        this.mAppWrapper = mAppWrapper;
    }

    public long getmUsdTrafficInMonth() {
        return mUsdTrafficInMonth;
    }

    public void setmUsdTrafficInMonth(long mUsdTrafficInMonth) {
        this.mUsdTrafficInMonth = mUsdTrafficInMonth;
    }

    public long getmBgUsedTrafficInMonth() {
        return mBgUsedTrafficInMonth;
    }

    public void setmBgUsedTrafficInMonth(long mBgUsedTrafficInMonth) {
        this.mBgUsedTrafficInMonth = mBgUsedTrafficInMonth;
    }

    public long getmSneakedTrafficInMonth() {
        return mSneakedTrafficInMonth;
    }

    public void setmSneakedTrafficInMonth(long mSneakedTrafficInMonth) {
        this.mSneakedTrafficInMonth = mSneakedTrafficInMonth;
    }

    public String getmFirewallType() {
       return FirewallType.getFireWallType(mFirewallType);
    }

    public void setmFirewallType(int mFirewallType) {
        this.mFirewallType = mFirewallType;
    }

    public long getmNetworkSpeed() {
        return mNetworkSpeed;
    }

    public void setmNetworkSpeed(long mNetworkSpeed) {
        this.mNetworkSpeed = mNetworkSpeed;
    }
}
