package com.tieto.systemmanagement.app.models;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.tieto.systemmanagement.app.utils.constants.AppListCache;

/**
 * Created by jinpei on 26/03/15.
 */
public class AppInfoModel {
    /**
     * The name of this app's package.
     */
    private String packageName;

    /**
     * The name of this app.
     */
    private String appLabel;

    /**
     * The version name of this app.
     */
    private String versionName;

    /**
     * The version code of this app.
     */
    private int versionCode;

    /**
     * The time at which the app was first installed.
     */
    private long firstInstallTime;

    /**
     * The time at which the app was last updated.
     */
    private long lastUpdateTime;

    /**
     * Package's install location.
     */
    private int installLocation;

    /**
     * Icon of app.
     */
    private Drawable icon;

    /**
     * 0 : Not system app.
     * 1 : System app.
     */
    private int flag;

    /**
     * Size of app's cache.
     */
    private long cacheSize;

    /**
     * Size of app's data.
     */
    private long dataSize;

    /**
     * Size of app's program.
     */
    private long programSize;

    /**
     * Size of app.
     * Equal to cacheSize + dataSize + programSize.
     */
    private long totalSize;

    private String codePath;

    /**
     * Is the size info set?
     */
    private boolean isSizeSet = false;

    public AppInfoModel(String packageName, String appLabel, String versionName, int versionCode, long firstInstallTime, int installLocation, long lastUpdateTime, Drawable icon, int flag, long cacheSize, long dataSize, long programSize, long totalSize, String codePath) {
        this.packageName = packageName;
        this.appLabel = appLabel;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.firstInstallTime = firstInstallTime;
        this.installLocation = installLocation;
        this.lastUpdateTime = lastUpdateTime;
        this.icon = icon;
        this.flag = flag;
        this.cacheSize = cacheSize;
        this.dataSize = dataSize;
        this.programSize = programSize;
        this.totalSize = totalSize;
        this.codePath = codePath;

        isSizeSet = true;

        AppListCache.AppListItemModelCache.put(packageName, this);
    }

    public AppInfoModel(PackageInfo packageinfo, AppSizeModel appSize, PackageManager pm) {
        this.packageName = packageinfo.packageName;
        this.appLabel = packageinfo.applicationInfo.loadLabel(pm).toString();
        this.versionName = packageinfo.versionName;
        this.versionCode = packageinfo.versionCode;
        this.firstInstallTime = packageinfo.firstInstallTime;
        this.installLocation = packageinfo.installLocation;
        this.lastUpdateTime = packageinfo.lastUpdateTime;
        this.icon = packageinfo.applicationInfo.loadIcon(pm);
        this.flag = packageinfo.applicationInfo.flags;
        this.cacheSize = appSize.getCacheSize();
        this.dataSize = appSize.getDataSize();
        this.programSize = appSize.getProgramSize();
        this.totalSize = appSize.getTotalSize();
        this.codePath = packageinfo.applicationInfo.dataDir;

        isSizeSet = true;

        AppListCache.AppListItemModelCache.put(packageName, this);
    }

    public AppInfoModel(PackageInfo packageinfo, PackageManager pm) {
        this.packageName = packageinfo.packageName;
        this.appLabel = packageinfo.applicationInfo.loadLabel(pm).toString();
        this.versionName = packageinfo.versionName;
        this.versionCode = packageinfo.versionCode;
        this.firstInstallTime = packageinfo.firstInstallTime;
        this.installLocation = packageinfo.installLocation;
        this.lastUpdateTime = packageinfo.lastUpdateTime;
        this.icon = packageinfo.applicationInfo.loadIcon(pm);
        this.flag = packageinfo.applicationInfo.flags;

        AppListCache.AppListItemModelCache.put(packageName, this);
    }

    public synchronized void setSizeInfo(AppSizeModel appSize) {
        this.cacheSize = appSize.getCacheSize();
        this.dataSize = appSize.getDataSize();
        this.programSize = appSize.getProgramSize();
        this.totalSize = appSize.getTotalSize();

        isSizeSet = true;
        AppListCache.AppListItemModelCache.put(packageName, this);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public long getFirstInstallTime() {
        return firstInstallTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public int getInstallLocation() {
        return installLocation;
    }

    public Drawable getIcon() {
        return icon;
    }

    public int getFlag() {
        return flag;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public long getDataSize() {
        return dataSize;
    }

    public long getProgramSize() {
        return programSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public boolean isSizeSet() {
        return isSizeSet;
    }

}
