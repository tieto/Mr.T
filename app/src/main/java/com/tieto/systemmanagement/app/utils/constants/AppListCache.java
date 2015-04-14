package com.tieto.systemmanagement.app.utils.constants;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;

import com.tieto.systemmanagement.app.models.AppInfoModel;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by jinpei on 31/03/15.
 */
public class AppListCache {

    public static List<PackageInfo> PackageInfoListCache = null;

    public static Map<String, AppInfoModel> AppListItemModelCache = new WeakHashMap<String, AppInfoModel>();

    public static List<ActivityManager.RunningAppProcessInfo> RunningAppProcessCache = null;

    public static boolean clearCache() {
        PackageInfoListCache = null;
        RunningAppProcessCache = null;
        AppListItemModelCache = new WeakHashMap<String, AppInfoModel>();
        return true;
    }

}
