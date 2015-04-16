package com.tieto.systemmanagement.app.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.RemoteException;

import com.tieto.systemmanagement.app.AppDetailActivity;
import com.tieto.systemmanagement.app.models.AppSizeModel;
import com.tieto.systemmanagement.app.models.ApplicationsState;

import java.lang.reflect.Method;


/**
 * Created by jinpei on 08/04/15.
 */
public class AppManagementTool {

    public static boolean Uninstall(Activity activity, String packageName) {
        try {
            Uri uri = Uri.parse("package:" + packageName);
            Intent intent = new Intent(Intent.ACTION_DELETE, uri);
            intent.putExtra(Intent.ACTION_UNINSTALL_PACKAGE, true);
            activity.startActivityForResult(intent, AppDetailActivity.REQUEST_UNINSTALL);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ForceStop(Context context, String packageName) {
        try {
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            Method forceStopPackage = am.getClass().getMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, packageName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ClearData(Activity activity, String packageName) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void ClearCache(Activity activity, final String packageName) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean deleteDatabase() {
        return true;
    }

    private static boolean deleteFile() {
        return true;
    }

    /**
     * A thread to get app size info.
     */
    private static class AppSizeInfoSetterRunnable implements Runnable {

        private PackageManager pm;
        private String packageName;

        private AppSizeInfoSetterRunnable(PackageManager pm, String packageName) {
            this.pm = pm;
            this.packageName = packageName;
        }

        @Override
        public void run() {
            AppSizeModel appSizeModel = new AppSizeModel();
            AppInfoTool.invokeGetAppSizeInfo(pm, packageName, new IPackageStatsObserver.Stub() {
                @Override
                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                    AppSizeModel appSizeModel = new AppSizeModel();
                    appSizeModel.setCacheSize(pStats.cacheSize);
                    appSizeModel.setDataSize(pStats.dataSize);
                    appSizeModel.setProgramSize(pStats.codeSize);
                    appSizeModel.setPackageName(pStats.packageName);

                    ApplicationsState.getInstance().sizeChanged(appSizeModel);
                }
            });
        }
    }
}
