package com.tieto.systemmanagement.diskmonitor.data;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.diskmonitor.entity.DStorageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangbo on 4/3/15.
 */
public class DiskData {
    private static final long KILOBYTE = 1024;
    private static DiskData ourInstance = new DiskData();
    private long internalTotal = 0 ;
    private long externalTotal = 0;

    public static DiskData getInstance() {
        return ourInstance;
    }

    public long getStorageTotal() {
        return  internalTotal + externalTotal;

    }

    private DiskData() {
        getStorageUsed();
    }

    public long getStorageUsed() {
        StatFs internalStatFs = new StatFs( Environment.getRootDirectory().getAbsolutePath() );
        long internalFree;

        StatFs externalStatFs = new StatFs( Environment.getExternalStorageDirectory().getAbsolutePath() );
        long externalFree;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            internalTotal = ( internalStatFs.getBlockCountLong() * internalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE * KILOBYTE );
            internalFree = ( internalStatFs.getAvailableBlocksLong() * internalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE * KILOBYTE );
            externalTotal = ( externalStatFs.getBlockCountLong() * externalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE * KILOBYTE );
            externalFree = ( externalStatFs.getAvailableBlocksLong() * externalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE * KILOBYTE );
        }
        else {
            internalTotal = ( (long) internalStatFs.getBlockCount() * (long) internalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE * KILOBYTE );
            internalFree = ( (long) internalStatFs.getAvailableBlocks() * (long) internalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE * KILOBYTE  );
            externalTotal = ( (long) externalStatFs.getBlockCount() * (long) externalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE * KILOBYTE );
            externalFree = ( (long) externalStatFs.getAvailableBlocks() * (long) externalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE * KILOBYTE );
        }

        return internalTotal + externalTotal -(internalFree + externalFree);
    }

    public long getMemPercentUsed() {
        return 100*(getStorageUsed())/getStorageTotal();
    }

    public List<DStorageInfo> getStoreSpaceInfos() {
        ArrayList<DStorageInfo> spaceInfos = new ArrayList<DStorageInfo>();

        DStorageInfo info1= new DStorageInfo();
        info1.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_apk));
        info1.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_1));
        info1.setTotal("123M");
        spaceInfos.add(info1);

        DStorageInfo info2= new DStorageInfo();
        info2.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_audio));
        info2.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_2));
        info2.setTotal("123M");
        spaceInfos.add(info2);

        DStorageInfo info3= new DStorageInfo();
        info3.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_zip));
        info3.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_3));
        info3.setTotal("123M");
        spaceInfos.add(info3);

        return spaceInfos;
    }

    public List<DStorageInfo> getSystemSpaceInfos() {
        ArrayList<DStorageInfo> systemInfos = new ArrayList<DStorageInfo>();

        DStorageInfo info1= new DStorageInfo();
        info1.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_media_uninstall));
        info1.setTitle(TApp.getInstance().getString(R.string.disk_space_system_title_1));
        info1.setTotal("");
        systemInfos.add(info1);

        return systemInfos;
    }


//    public ArrayList<DProcessInfo> getPackages() {
//        ArrayList<DProcessInfo> apps = getInstalledApps(false); /* false = no system packages */
//        final int max = apps.size();
//        for (int i=0; i<max; i++) {
//            apps.get(i).prettyPrint();
//        }
//        return apps;
//    }
//
//    public ArrayList<DProcessInfo> getInstalledApps(boolean getSysPackages) {
//        ArrayList<DProcessInfo> res = new ArrayList<DProcessInfo>();
//        List<PackageInfo> packs = TApp.getInstance().getPackageManager().getInstalledPackages(0);
//        for(int i=0;i<packs.size();i++) {
//            PackageInfo p = packs.get(i);
//            if ((!getSysPackages) && (p.versionName == null)) {
//                continue ;
//            }
//
//            DProcessInfo newInfo = new DProcessInfo();
//            newInfo.appname = p.applicationInfo.loadLabel(TApp.getInstance().getPackageManager()).toString();
//            newInfo.pname = p.packageName;
//            newInfo.versionName = p.versionName;
//            newInfo.versionCode = p.versionCode;
//            newInfo.path = p.applicationInfo.sourceDir;
//            newInfo.icon = p.applicationInfo.loadIcon(TApp.getInstance().getPackageManager());
//
//            File file = new File(newInfo.path);
//            long sizeinByte = file.length();
//            long sizeinMb = sizeinByte / (1024*1024);
//
//            Toast.makeText(TApp.getInstance(), newInfo.appname + "\t"/*"\t" + newInfo.pname + "\t" + newInfo.versionName + "\t" + newInfo.versionCode + "\t" */ + sizeinMb + "Mb", Toast.LENGTH_SHORT).show();
//            res.add(newInfo);
//        }
//        return res;
//    }
}
