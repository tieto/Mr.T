package com.tieto.systemmanagement.app.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;

import com.tieto.systemmanagement.app.model.AppSizeModel;

import java.io.File;
import java.lang.reflect.Method;


/**
 * Created by jinpei on 08/04/15.
 */
public class AppManagementTool {

    public static final int REQUEST_UNINSTALL = 1;

    public static boolean Uninstall(Activity activity, String packageName) {
        try {
            Uri uri = Uri.parse("package:" + packageName);
            Intent intent = new Intent(Intent.ACTION_DELETE, uri);
            intent.putExtra(Intent.ACTION_UNINSTALL_PACKAGE, true);
            activity.startActivityForResult(intent, REQUEST_UNINSTALL);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ForceStop(Context context, String packageName) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            am.killBackgroundProcesses(packageName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ClearData(Activity activity, String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_DEFAULT);
            intent.setClassName(activity.getPackageName(),
                    "");
            activity.startActivityForResult(intent, 2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void ClearCache(Activity activity, final String packageName) {
        final PackageManager pm = activity.getPackageManager();
        try {
            Method method = pm.getClass().getMethod("freeStorageAndNotify", Long.TYPE, IPackageDataObserver.class);
            Long freeStorageSize = Long.valueOf(getEnvironmentSize() - 1L);

            method.invoke(pm, freeStorageSize, new IPackageDataObserver.Stub() {
                @Override
                public void onRemoveCompleted(String name, boolean succeeded) throws RemoteException {
                    new Thread(new AppSizeInfoSetterRunnable(pm, packageName)).start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long getEnvironmentSize() {
        File localFile = Environment.getDataDirectory();
        long L1;

        if (localFile == null)
            L1 = 0L;

        while (true) {
            String str = localFile.getPath();
            StatFs localStatFs = new StatFs(str);
            long L2 = localStatFs.getBlockSizeLong();
            L1 = localStatFs.getBlockCountLong() * L2;
            return L1;
        }
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
