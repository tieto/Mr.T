package com.tieto.systemmanagement.app.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Message;
import android.os.RemoteException;

import com.tieto.systemmanagement.app.adapters.AppListAdapter;
import com.tieto.systemmanagement.app.fragments.AppListFragment;
import com.tieto.systemmanagement.app.models.AppInfoModel;
import com.tieto.systemmanagement.app.models.AppSizeModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinpei on 26/03/15.
 */
public class AppListFragmentTool {

    /**
     * Get downloaded app information list.
     */
    public final static int APP_LIST_TYPE_DOWNLOADED = 0;

    /**
     * Get running app information list.
     */
    public final static int APP_LIST_TYPE_RUNNING = 1;

    /**
     * Get all app information list.
     */
    public final static int APP_LIST_TYPE_ALL = 2;

    /**
     * Instance of app list fragment.
     */
    private WeakReference<AppListFragment> mAppListFragment;

    /**
     * Global parameters.
     */
    private PackageManager mPackageManager;
    private ActivityManager mActivityManager;
    private Context mContext;

//    /**
//     * Singleton mode.
//     */
//    private static AppInfoTool instance = null;
//
//    public static AppInfoTool getInstance(AppListFragment appListFragment) {
//        if(instance == null) {
//            instance = new AppInfoTool(appListFragment);
//        }
//
//        return instance;
//    }

    public AppListFragmentTool(AppListFragment appListFragment) {
        this.mAppListFragment = new WeakReference(appListFragment);
        if (mAppListFragment != null) {
            mContext = appListFragment.getActivity();
            mPackageManager = mContext.getPackageManager();
            mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        }
    }

    /**
     * Get adapter of application list by app list type.
     *
     * @return Returns an adapter of application list.
     */
    public AppListAdapter getAppListAdapter() {
        if (mAppListFragment.get() == null) return null;
        PackageManager pm = mAppListFragment.get().getActivity().getPackageManager();
        List<PackageInfo> packageInfoList = AppInfoTool.getAppList(pm);
        AppListAdapter mAppListAdapter = null;
        switch (mAppListFragment.get().getType()) {
            case APP_LIST_TYPE_DOWNLOADED:
                mAppListAdapter = getDownloadedAppListAdapter(packageInfoList);
                break;
            case APP_LIST_TYPE_RUNNING:
                mAppListAdapter = getRunningAppListAdapter();
                break;
            case APP_LIST_TYPE_ALL:
                mAppListAdapter = getAllAppListAdapter(packageInfoList);
                break;
        }
        return mAppListAdapter;
    }

    /**
     * Get list adapter of all app.
     *
     * @param packageInfoList
     * @return
     */
    private AppListAdapter getAllAppListAdapter(List<PackageInfo> packageInfoList) {

        List<AppInfoModel> appInfoModels = new ArrayList<AppInfoModel>();
        AppInfoModel appInfoModel = null;

        int size = packageInfoList.size();
        for (int i = 0; i < size; i++) {
            PackageInfo packageInfo = packageInfoList.get(i);

            try {
                appInfoModel = new AppInfoModel(packageInfo, mPackageManager);
                appInfoModels.add(appInfoModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new AppListAdapter(appInfoModels, mAppListFragment);
    }

    /**
     * Get list adapter of running app.
     *
     * @return
     */
    private AppListAdapter getRunningAppListAdapter() {

        List<AppInfoModel> appInfoModels = new ArrayList<AppInfoModel>();
        AppInfoModel appInfoModel = null;

        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = null;
        if (mActivityManager != null) {
            runningAppProcessInfo = AppInfoTool.getRunningAppProcessInfoList(mActivityManager);
            int size = runningAppProcessInfo.size();
            for (int i = 0; i < size; i++) {
                ActivityManager.RunningAppProcessInfo running = runningAppProcessInfo.get(i);
                String[] pkgNameList = running.pkgList;

                for (int j = 0; j < pkgNameList.length; j++) {
                    try {
                        PackageInfo runningPackageInfo = mPackageManager.getPackageInfo(pkgNameList[j], PackageManager.GET_UNINSTALLED_PACKAGES);
                        appInfoModel = new AppInfoModel(runningPackageInfo, mPackageManager);
                        appInfoModel.setUID(running.uid);
                        appInfoModels.add(appInfoModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return new AppListAdapter(appInfoModels, mAppListFragment);
    }

    /**
     * Get list adapter of downloaded app.
     *
     * @param packageInfoList
     * @return
     */
    private AppListAdapter getDownloadedAppListAdapter(List<PackageInfo> packageInfoList) {

        List<AppInfoModel> appInfoModels = new ArrayList<AppInfoModel>();
        AppInfoModel appInfoModel = null;

        int size = packageInfoList.size();
        for (int i = 0; i < size; i++) {
            PackageInfo packageInfo = packageInfoList.get(i);

            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                try {
                    appInfoModel = new AppInfoModel(packageInfo, mPackageManager);
                    appInfoModels.add(appInfoModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return new AppListAdapter(appInfoModels, mAppListFragment);
    }

    /**
     * Get app size info.
     *
     * @param packageName
     * @param position
     * @param appSizeInfoSetterHandler
     * @throws Exception
     */
    public void getAppSizeModel(String packageName, int position, AppListAdapter.AppSizeInfoSetterHandler appSizeInfoSetterHandler) throws Exception {
        AppSizeModel appSizeModel = new AppSizeModel();
        AppInfoTool.invokeGetAppSizeInfo(mPackageManager, packageName, new PackageSizeObserver(appSizeModel, position, appSizeInfoSetterHandler));
    }

    private class PackageSizeObserver extends IPackageStatsObserver.Stub {

        private AppSizeModel mAppSizeModel;

        private int position;

        private AppListAdapter.AppSizeInfoSetterHandler appSizeInfoSetterHandler;

        public PackageSizeObserver(AppSizeModel appSizeModel, int position, AppListAdapter.AppSizeInfoSetterHandler appSizeInfoSetterHandler) {
            mAppSizeModel = appSizeModel;
            this.position = position;
            this.appSizeInfoSetterHandler = appSizeInfoSetterHandler;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            AppListFragment appListFragment = mAppListFragment.get();
            if (succeeded) {
                mAppSizeModel.setCacheSize(pStats.cacheSize);
                mAppSizeModel.setDataSize(pStats.dataSize);
                mAppSizeModel.setProgramSize(pStats.codeSize);
                mAppSizeModel.setPackageName(pStats.packageName);

                Message msg = new Message();
                msg.arg1 = position;
                msg.obj = mAppSizeModel;

                appSizeInfoSetterHandler.sendMessage(msg);
            }
        }
    }
}
