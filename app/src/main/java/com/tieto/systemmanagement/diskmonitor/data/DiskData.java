package com.tieto.systemmanagement.diskmonitor.data;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.widget.Toast;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.diskmonitor.entity.DMemInfo;
import com.tieto.systemmanagement.diskmonitor.entity.DProcessInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangbo on 4/3/15.
 */
public class DiskData {
    private static ActivityManager.MemoryInfo outMemoryInfo = new ActivityManager.MemoryInfo();    private static DiskData ourInstance = new DiskData();
    public static DiskData getInstance() {
        return ourInstance;
    }

    public long getMemTotal () {
        return outMemoryInfo.totalMem/1048576L;
    }

    private DiskData() {
        getMemAvailable();
    }

    public long getMemAvailable() {
        ActivityManager activityManager = (ActivityManager) TApp.getInstance().getSystemService(Activity.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(outMemoryInfo);
        long availableMegs = outMemoryInfo.availMem / 1048576L;
        return availableMegs;
    }

    public long getMemPercentUserd() {
        long precent_used = 100*getMemAvailable()/getMemTotal();
        return precent_used;
    }

    public List<DMemInfo> getStoreSpaceInfos() {
        ArrayList<DMemInfo> spaceInfos = new ArrayList<DMemInfo>();

        DMemInfo info1= new DMemInfo();
        info1.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_apk));
        info1.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_1));
        info1.setTotal("123M");
        spaceInfos.add(info1);

        DMemInfo info2= new DMemInfo();
        info2.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_audio));
        info2.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_2));
        info2.setTotal("123M");
        spaceInfos.add(info2);

        DMemInfo info3= new DMemInfo();
        info3.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_zip));
        info3.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_3));
        info3.setTotal("123M");
        spaceInfos.add(info3);

        return spaceInfos;
    }

    public List<DMemInfo> getSystemSpaceInfos() {
        ArrayList<DMemInfo> systemInfos = new ArrayList<DMemInfo>();

        DMemInfo info1= new DMemInfo();
        info1.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_media_uninstall));
        info1.setTitle(TApp.getInstance().getString(R.string.disk_space_system_title_1));
        info1.setTotal("");
        systemInfos.add(info1);

        return systemInfos;
    }


    public ArrayList<DProcessInfo> getPackages() {
        ArrayList<DProcessInfo> apps = getInstalledApps(false); /* false = no system packages */
        final int max = apps.size();
        for (int i=0; i<max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    public ArrayList<DProcessInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<DProcessInfo> res = new ArrayList<DProcessInfo>();
        List<PackageInfo> packs = TApp.getInstance().getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }

            DProcessInfo newInfo = new DProcessInfo();
            newInfo.appname = p.applicationInfo.loadLabel(TApp.getInstance().getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.path = p.applicationInfo.sourceDir;
            newInfo.icon = p.applicationInfo.loadIcon(TApp.getInstance().getPackageManager());

            File file = new File(newInfo.path);
            long sizeinByte = file.length();
            long sizeinMb = sizeinByte / (1024*1024);

            Toast.makeText(TApp.getInstance(), newInfo.appname + "\t"/*"\t" + newInfo.pname + "\t" + newInfo.versionName + "\t" + newInfo.versionCode + "\t" */ + sizeinMb + "Mb", Toast.LENGTH_SHORT).show();
            res.add(newInfo);
        }
        return res;
    }
}
