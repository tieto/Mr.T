package com.tieto.systemmanagement.app.tools;

import android.app.ActivityManager;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tieto.systemmanagement.app.constants.AppListCache;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by jinpei on 31/03/15.
 */
public class AppInfoTool {

    /**
     * Method name.
     */
    private final static String GET_PACKAGE_SIZE_INFO = "getPackageSizeInfo";

    public static synchronized List<PackageInfo> getAppList(PackageManager pm) {
        if (AppListCache.PackageInfoListCache == null) {
            try {
                AppListCache.PackageInfoListCache = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return AppListCache.PackageInfoListCache;
    }

    public static synchronized List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessInfoList(ActivityManager am) {
        if (AppListCache.RunningAppProcessCache == null) {
            try {
                AppListCache.RunningAppProcessCache = am.getRunningAppProcesses();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return AppListCache.RunningAppProcessCache;
    }

    public static void invokeGetAppSizeInfo(PackageManager pm, String packageName, IPackageStatsObserver observer) {
        try {
            Method getPackageSizeInfo = pm.getClass().getMethod(GET_PACKAGE_SIZE_INFO, String.class, IPackageStatsObserver.class);
            getPackageSizeInfo.setAccessible(true);
            getPackageSizeInfo.invoke(pm, packageName, observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
