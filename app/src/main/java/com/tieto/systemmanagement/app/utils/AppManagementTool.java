package com.tieto.systemmanagement.app.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.app.AppDetailActivity;
import com.tieto.systemmanagement.app.models.AppSizeModel;
import com.tieto.systemmanagement.app.models.ApplicationsState;

import java.io.DataOutputStream;
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

    public static void ForceStop(Activity context, String packageName) {
        boolean stopped = false;
        if(context.checkPermission("android.permission.FORCE_STOP_PACKAGES", android.os.Process.myPid(), android.os.Process.myUid()) != PackageManager.PERMISSION_GRANTED) {
            createNotRootDialog(context);
            return;
        } else {
            try {
                Class[] arrayOfClass = new Class[1];
                arrayOfClass[0] = String.class;
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                Method forceStopPackage = ActivityManager.class.getMethod("forceStopPackage", arrayOfClass);
                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0] = packageName;
                forceStopPackage.invoke(am, arrayOfObject);
                stopped = true;
            } catch (Exception e) {
                e.printStackTrace();
                stopped = false;
            }
        }
        ApplicationsState.getInstance().runningStateChanged(packageName, !stopped);
    }

    public static boolean CommandByADB(String command) {
        java.lang.Process process = null;
        DataOutputStream dos = null;
        try {
            process = Runtime.getRuntime().exec("su -v");
            Log.d("process get completely", command);
            dos = new DataOutputStream(process.getOutputStream());
            dos.write(command.getBytes());
            dos.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {

            }
        }
        return true;
    }

    public static void ClearData(Activity activity, final String pName) {
        if(activity.checkPermission("android.permission.CLEAR_APP_USER_DATA", android.os.Process.myPid(), android.os.Process.myUid()) != PackageManager.PERMISSION_GRANTED){
            createNotRootDialog(activity);
            return;
        }

        try {
            Class[] arrayOfClass = new Class[2];
            arrayOfClass[0] = String.class;
            arrayOfClass[1] = IPackageDataObserver.class;
            final PackageManager pm = activity.getPackageManager();
            ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            Method clearApplicationUserData = ActivityManager.class.getMethod("clearApplicationUserData", arrayOfClass);
            Object[] arrayOfObject = new Object[2];
            arrayOfObject[0] = pName;
            arrayOfObject[1] = new IPackageDataObserver.Stub() {
                @Override
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                    new Thread(new AppSizeInfoSetterRunnable(pm, pName)).start();
                }
            };
            clearApplicationUserData.invoke(am, arrayOfObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ClearCache(Activity activity, final String pName) {
        if(activity.checkPermission("android.permission.DELETE_CACHE_FILES", android.os.Process.myPid(), android.os.Process.myUid()) != PackageManager.PERMISSION_GRANTED){
            createNotRootDialog(activity);
            return;
        }
        boolean cleared = false;
            try {
                Class[] arrayOfClass = new Class[2];
                arrayOfClass[0] = String.class;
                arrayOfClass[1] = IPackageDataObserver.class;
                final PackageManager pm = (PackageManager) activity.getPackageManager();
                Method deleteApplicationCacheFiles = PackageManager.class.getMethod("deleteApplicationCacheFiles", arrayOfClass);
                Object[] arrayOfObject = new Object[2];
                arrayOfObject[0] = pName;
                arrayOfObject[1] = new IPackageDataObserver.Stub() {
                    @Override
                    public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                        new Thread(new AppSizeInfoSetterRunnable(pm, pName)).start();
                    }
                };
                deleteApplicationCacheFiles.invoke(pm, arrayOfObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void createNotRootDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.app_notice)
                .setMessage(R.string.app_have_not_been_root).show();
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
