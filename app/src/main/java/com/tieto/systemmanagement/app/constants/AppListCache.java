package com.tieto.systemmanagement.app.constants;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;

import com.tieto.systemmanagement.app.model.AppListItemModel;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jinpei on 31/03/15.
 */
public class AppListCache {

    public static List<PackageInfo> PackageInfoListCache = null;

    public static Map<String, AppListItemModel> AppListItemModelCache = new WeakHashMap<String, AppListItemModel>();

    public static List<ActivityManager.RunningAppProcessInfo> RunningAppProcessCache = null;

    public static ExecutorService ExecutorService = Executors.newSingleThreadExecutor();

    public static boolean clearCache() {
        PackageInfoListCache = null;
        RunningAppProcessCache = null;
        AppListItemModelCache = new WeakHashMap<String, AppListItemModel>();
        return true;
    }

}
