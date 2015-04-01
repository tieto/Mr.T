package com.tieto.systemmanagement.trafficmonitor.entity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import com.tieto.systemmanagement.trafficmonitor.storage.AppNetInfoPreferrence;

import java.util.ArrayList;
import java.util.List;


//1,how to save the value of isNetworkAllowed for each app.
//2,


/**
 * Created by jane on 15-3-24.
 */
public class TrafficStatsImpl {
    private static final int ALL = 0;
    private static final int BACKGROUND = 1;
    private TrafficStats mTrafficStats;
    private PackageManager mPackageManager;
    private Context context;

    private float TrafficLeft;
    private List<AppInfoEntity> trafficInfoForEachApp;

    public TrafficStatsImpl(Context context) {
        this.context = context;
        mPackageManager = context.getPackageManager();
        mTrafficStats = new TrafficStats();
    }


    /**
     * get the left traffic in the month
     * the total value and the traffic used for every boot should be saved in sharePreference TrafficQuotesPref
     * @return
     */
    public float getTrafficLeft() {
        return TrafficLeft;
    }

    /**
     * get all the info about each app
     * @return
     */
    public List<AppInfoEntity> getTrafficInfoForEachApp() {
        trafficInfoForEachApp = new ArrayList<AppInfoEntity>();
        List<PackageInfo> packageInfos = mPackageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);

        for(PackageInfo p:packageInfos) {
            String[] permissions = p.requestedPermissions;
            if(permissions!=null) {
                for (String permission : permissions) {
                    if (permission.equals(android.Manifest.permission.INTERNET)) {
                        ApplicationInfo appInfo = p.applicationInfo;
                        if ((appInfo.flags & ApplicationInfo.FLAG_INSTALLED) > 0) {
                            //0-non-system   1-system
                            // ( appInfo.flags & ApplicationInfo.FLAG_SYSTEM )>0---system app,here we get all apps
                            AppInfoEntity appInfoEntiy = new AppInfoEntity();
                            appInfoEntiy.setAppIcon(appInfo.loadIcon(mPackageManager));
                            appInfoEntiy.setAppName(appInfo.loadLabel(mPackageManager).toString() + "-" + appInfo.uid);
//                myAppInfo.setAppName(mPackageManager.getNameForUid(appInfo.uid));
                            int uid = appInfo.uid;
                            appInfoEntiy.setUid(uid);

                            appInfoEntiy.setAppNetSpeeed(getAppNetSpeed(uid));
                            appInfoEntiy.setAppTrafficUsedBg(getAppTrafficUsed(uid, BACKGROUND));
                            appInfoEntiy.setAppTrafficUsed(getAppTrafficUsed(uid, ALL));
                            appInfoEntiy.setIsNetworkAllowed(getIsNetWorkAllowed(uid));

                            trafficInfoForEachApp.add(appInfoEntiy);
                        }
                    }
                }
            }
        }

        return trafficInfoForEachApp;
    }

    /**
     * get the value whether the app are allowed to connect network
     * @param uid  the app uid
     * @return 1-sneaking prohibited 2-network prohibited;3-network allowed ; 4-wifi allowed only
     */
    private int getIsNetWorkAllowed(int uid) {
        //here we may save the value in the sharePreference  AppNetworkInfoPref
        int netState = AppNetInfoPreferrence.getAppNetState(context,uid);
        return netState;
    }

    /**
     * get the traffic value the given uid app used
     * @param uid the app uid
     * @param flag 0-all traffic used ; 1-traffic used background
     * @return long
     */
    private long getAppTrafficUsed(int uid, int flag) {
        long total = 0;
        switch (flag){
            case 0://get the traffic total the app used
                //NOTE: the value we get is start from boot ,here we want to get the month.
                long rx = mTrafficStats.getUidRxBytes(uid);
                long tx = mTrafficStats.getUidTxBytes(uid);
                total = rx+tx;
                break;
            case 1://get the traffic total the app used background
                break;
        }

        long mRx =  mTrafficStats.getMobileRxBytes();
        long mTx = mTrafficStats.getUidTxBytes(uid);

        return total;
    }

    /**
     * get the given uid app's network speed
     * @param uid
     * @return
     */
    private int getAppNetSpeed(int uid) {
        return 0;
    }

}
