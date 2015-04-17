package com.tieto.systemmanagement.trafficmonitor.entity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;

import com.tieto.systemmanagement.authority.entity.AppWrapper;
import com.tieto.systemmanagement.trafficmonitor.storage.TrafficMonitorPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jane on 15-3-24.
 */
public class TrafficStatsWrapper {
    private PackageManager mPackageManager;
    private Context mContext;

    public TrafficStatsWrapper(Context context) {
        this.mContext = context;
        mPackageManager = context.getPackageManager();
    }

    // all the non-system app which has network permission
    public Map<Integer,ApplicationInfo> getListAppMap() {
        Map<Integer,ApplicationInfo> listAppMap = new HashMap<Integer,ApplicationInfo>();

        List<PackageInfo> packageInfo = mPackageManager.getInstalledPackages(
                PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        for (PackageInfo p : packageInfo) {
            String[] permissions = p.requestedPermissions;
            if (permissions != null) {
                for (String permission : permissions) {
                    if (permission.equals(android.Manifest.permission.INTERNET)) {
                        ApplicationInfo appInfo = p.applicationInfo;
                        if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                        } else {
                            listAppMap.put(appInfo.uid,appInfo);
                        }
                    }
                }
            }
        }
        return listAppMap;
    }


    // get the app traffic info list
    public List<AppTrafficInfo> getAppTrafficInfoList() {
        List<AppTrafficInfo> appTrafficInfoList = new ArrayList<AppTrafficInfo>();

        Iterator iterator = getListAppMap().entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Integer,ApplicationInfo> entry = (Map.Entry<Integer, ApplicationInfo>)
                    iterator.next();

            int uid = entry.getKey();
            ApplicationInfo appInfo = entry.getValue();
            AppTrafficInfo appTrafficInfo = new AppTrafficInfo();
            AppWrapper appWrapper = new AppWrapper(appInfo);
            appTrafficInfo.setmAppWrapper(appWrapper);
            appTrafficInfo.setmNetworkSpeed(getAppNetSpeed(uid));
            appTrafficInfo.setmBgUsedTrafficInMonth(getAppTrafficUsed(
                    uid, TrafficMonitorPref.FLAG_UID_BACKGROUND));
            appTrafficInfo.setmUsdTrafficInMonth(getAppTrafficUsed(
                    uid, TrafficMonitorPref.FLAG_UID_TOTAL));
            appTrafficInfo.setmFirewallType(getFirewallType(uid));

            appTrafficInfoList.add(appTrafficInfo);
        }
        return appTrafficInfoList;
    }

    /**
     * get the value whether the app are allowed to connect network
     * @param uid  the app uid
     * @return 1-sneaking prohibited 2-network prohibited;3-network allowed ; 4-wifi allowed only
     */
    private int getFirewallType(int uid) {
        //here we may save the value in the sharePreference  AppNetworkInfoPref
        int netState = (int) TrafficMonitorPref.get(
                mContext,TrafficMonitorPref.FLAG_FIREWALL_TYPE,uid);
        return netState;
    }

    /**
     * get the traffic value the given uid app used
     * @param uid the app uid
     * @param flag 0-all traffic used ; 1-traffic used background
     * @return long
     */
    private long getAppTrafficUsed(int uid, int flag) {
        TrafficStats trafficStatic = new TrafficStats();
        long total = 0;
        switch (flag){
            //get the traffic total the app used
            case TrafficMonitorPref.FLAG_TOTAL:
                // get the total traffic used which saved in sharePreference
                total = TrafficMonitorPref.get(
                        mContext, TrafficMonitorPref.FLAG_TOTAL, -1);
                //add the total traffic used since this boot
                total += trafficStatic.getTotalRxBytes();
                break;
            case TrafficMonitorPref.FLAG_UID_TOTAL:
                //get the traffic value saved in the sharePreference
                total = TrafficMonitorPref.get(
                        mContext, TrafficMonitorPref.FLAG_UID_TOTAL, uid);
                //add the traffic used since this boot
                total += trafficStatic.getUidRxBytes(uid);
                break;
            case TrafficMonitorPref.FLAG_UID_BACKGROUND:
                total = TrafficMonitorPref.get(
                        mContext, TrafficMonitorPref.FLAG_UID_BACKGROUND, uid);
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
