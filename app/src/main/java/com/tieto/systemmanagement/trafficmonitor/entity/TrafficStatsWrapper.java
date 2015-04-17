package com.tieto.systemmanagement.trafficmonitor.entity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;

import com.tieto.systemmanagement.trafficmonitor.storage.FirewallTypePreferrence;
import com.tieto.systemmanagement.trafficmonitor.storage.TrafficStasticPreferrence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jane on 15-3-24.
 */
public class TrafficStatsWrapper {
    private TrafficStats mTrafficStats;
    private PackageManager mPackageManager;
    private Context mContext;
    //本月剩余流量
    private float mTrafficLeft;
    //app流量信息列表
    private List<AppInfoEntity> mTrafficStaticAppInfoLists;


    public TrafficStatsWrapper(Context context) {
        this.mContext = context;
        mPackageManager = context.getPackageManager();
        mTrafficStats = new TrafficStats();
    }


    /**
     * get the left traffic in the month
     * the total value and the traffic used for every boot should be saved in sharePreference TrafficQuotesPref
     * @return
     */
    public float getTrafficLeft() {
        return mTrafficLeft;
    }

    /**
     * get all the info about each app
     * @return
     */
    public List<AppInfoEntity> getmTrafficStaticAppInfoLists() {
        mTrafficStaticAppInfoLists = new ArrayList<AppInfoEntity>();

        //获取具有联网权限的APP
        List<PackageInfo> packageInfos = mPackageManager.getInstalledPackages(
                PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);

        for(PackageInfo p:packageInfos) {
            String[] permissions = p.requestedPermissions;
            if(permissions!=null) {
                for (String permission : permissions) {
                    if (permission.equals(android.Manifest.permission.INTERNET)) {
                        ApplicationInfo appInfo = p.applicationInfo;
                        //0-non-system   1-system
                        //0-10000 is kept for system app , and for non-system app ,uid>10000
                        if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                           //system app ,ignore
                        } else {
                            // ( appInfo.flags & ApplicationInfo.FLAG_SYSTEM )>
                            // 0---system app,here we get all apps
                            AppInfoEntity appInfoEntiy = new AppInfoEntity();
                            appInfoEntiy.setmAppIcon(appInfo.loadIcon(mPackageManager));
                            appInfoEntiy.setmAppName(appInfo.loadLabel(
                                    mPackageManager).toString()+appInfo.uid);
                            int uid = appInfo.uid;
                            appInfoEntiy.setUid(uid);

                            appInfoEntiy.setmAppNetSpeeed(getAppNetSpeed(uid));
                            appInfoEntiy.setmAppTrafficUsedBg(getAppTrafficUsed(
                                    uid, TrafficStasticPreferrence.FLAG_UID_BACKGROUND));
                            appInfoEntiy.setmAppTrafficUsed(getAppTrafficUsed(
                                    uid, TrafficStasticPreferrence.FLAG_UID_TOTAL));
                            appInfoEntiy.setFirewallType(getAppNetworkConnectType(uid));

                            mTrafficStaticAppInfoLists.add(appInfoEntiy);
                        }

                    }
                }
            }
        }

        return mTrafficStaticAppInfoLists;
    }

    /**
     * get the value whether the app are allowed to connect network
     * @param uid  the app uid
     * @return 1-sneaking prohibited 2-network prohibited;3-network allowed ; 4-wifi allowed only
     */
    private int getAppNetworkConnectType(int uid) {
        //here we may save the value in the sharePreference  AppNetworkInfoPref
        int netState = FirewallTypePreferrence.getNetworkState(mContext, uid);
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
            //get the traffic total the app used
            case TrafficStasticPreferrence.FLAG_TOTAL:
                // get the total traffic used which saved in sharePreference
                total = TrafficStasticPreferrence.get(
                        mContext,TrafficStasticPreferrence.FLAG_TOTAL,-1);
                //add the total traffic used since this boot
                total += mTrafficStats.getTotalRxBytes();
                break;
            case TrafficStasticPreferrence.FLAG_UID_TOTAL:
                //get the traffic value saved in the sharePreference
                total = TrafficStasticPreferrence.get(
                        mContext,TrafficStasticPreferrence.FLAG_UID_TOTAL,uid);
                //add the traffic used since this boot
                total += mTrafficStats.getUidRxBytes(uid);
                Log.e("TAG","UID:"+uid+",total:"+total+",now:"+mTrafficStats.getUidRxBytes(uid));
                break;
            case TrafficStasticPreferrence.FLAG_UID_BACKGROUND:
                total = TrafficStasticPreferrence.get(
                        mContext,TrafficStasticPreferrence.FLAG_UID_BACKGROUND,uid);
                break;
        }
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
