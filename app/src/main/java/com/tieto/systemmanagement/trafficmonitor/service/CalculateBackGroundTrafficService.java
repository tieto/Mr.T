package com.tieto.systemmanagement.trafficmonitor.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.IBinder;
import android.util.Log;

import com.tieto.systemmanagement.trafficmonitor.entity.TrafficStatsWrapper;
import com.tieto.systemmanagement.trafficmonitor.storage.TrafficMonitorPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jane on 15-4-15.
 */
public class CalculateBackGroundTrafficService extends Service {
    //check the background app list every 1 sec.
    private static final long SCAN_INTERVAL = 1000*10;

    private TrafficStats mTrafficStats;
    private ActivityManager mManager;
    private PackageManager mPackageManager;

    //all apps running in the background which listed
    private List<ApplicationInfo> mBackgroundRunningApplication;
    //map to keep the uid-traffic when calculate traffic in background
    private Map<Integer,Long> mUidTrafficMaps;
    //whether is the first time to check.
    private boolean isFirstCheck;
    private String TAG = "CalculateBackGroundTrafficService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mTrafficStats = new TrafficStats();
        mUidTrafficMaps = new HashMap<Integer,Long>();
        isFirstCheck = true;
        mManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        mPackageManager = this.getPackageManager();

        mBackgroundRunningApplication = new ArrayList<ApplicationInfo>();

        //每次开机时便启动该服务
        //扫描后台运行的进程，列出运行在这写进程中的application
        //记录第一次检测到该app时，所使用的流量 usedTrafficStart
        //记录该应用退出后台时，所使用的流量 usedTrafficEnd
        //保存后台使用的流量
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true) {
                    mBackgroundRunningApplication = searchAllBackgroundApp();
                    checkAndSave();
                    try {
                        sleep(SCAN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * check the background running app list , and update the uid-traffic map
     */
    private void checkAndSave() {
        if(!isFirstCheck) {
            // remove the uid-traffic which exit from the background process list
            // and save the info into sharePreference
            Iterator iterator = mUidTrafficMaps.entrySet().iterator();
            while(iterator!=null && iterator.hasNext()){
                Map.Entry<Integer,Long> entry = (Map.Entry<Integer, Long>) iterator.next();
                int uid = entry.getKey();
                //deleted apps which exit to background and save data
                if(!mBackgroundRunningApplication.contains(uid)) {
                    long trafficUsedEnd = mTrafficStats.getUidRxBytes(uid);
                    long trafficUsedBackground = trafficUsedEnd - mUidTrafficMaps.get(uid);
                    //save to sharePref
                    TrafficMonitorPref.save(this, trafficUsedBackground,
                            TrafficMonitorPref.FLAG_UID_BACKGROUND, uid);
                    //remove the uid-traffic
                    iterator.remove();
                }
            }
            //add new uid-traffic when new app join in the background process list
            for(ApplicationInfo applicationInfo:mBackgroundRunningApplication) {
                int uid = applicationInfo.uid;
                if(!mUidTrafficMaps.containsKey(uid)) {
                    long trafficUsedStart = mTrafficStats.getUidRxBytes(uid);
                    mUidTrafficMaps.put(uid, trafficUsedStart);
                }
            }

        } else {
            for(ApplicationInfo applicationInfo:mBackgroundRunningApplication) {
                int uid = applicationInfo.uid;
                long trafficUsedStart = mTrafficStats.getUidRxBytes(uid);
                mUidTrafficMaps.put(uid, trafficUsedStart);
            }
            isFirstCheck = false;
        }
    }

    /**
     * search all the apps running in the background
     * @return
     */
    private List<ApplicationInfo> searchAllBackgroundApp() {
        // all running processes
        ArrayList<ActivityManager.RunningAppProcessInfo> backgroundRunningProcess =
                getBackgroundRunningAppInfos();

        Map<Integer,ApplicationInfo> applicationInfosMaps =
                new HashMap<Integer,ApplicationInfo>();
        applicationInfosMaps = new TrafficStatsWrapper(this).getListAppMap();

        //all the list apps which runs in the background
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo:backgroundRunningProcess) {
            int uid = runningAppProcessInfo.uid;
            if (applicationInfosMaps.containsKey(uid)) {
                mBackgroundRunningApplication.add((ApplicationInfo) applicationInfosMaps.get(uid));
            }
        }
        return mBackgroundRunningApplication;
    }

    // all background running applications
    private ArrayList<ActivityManager.RunningAppProcessInfo> getBackgroundRunningAppInfos() {
        List<ActivityManager.RunningAppProcessInfo> runningProcesses =
                mManager.getRunningAppProcesses();
        // all the running process in the background
        ArrayList<ActivityManager.RunningAppProcessInfo> backgroundRunningProcess =
                new ArrayList<ActivityManager.RunningAppProcessInfo>();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningProcesses) {
            int importance = runningAppProcessInfo.importance;
            //all running process in the backgroundProcess
            if (ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND == importance) {
                backgroundRunningProcess.add(runningAppProcessInfo);
            }
        }
        return backgroundRunningProcess;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkAndSave();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
}
