package com.tieto.systemmanagement.diskmonitor.data;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.diskmonitor.entity.AudioInfo;
import com.tieto.systemmanagement.diskmonitor.entity.StorageInfo;
import com.tieto.systemmanagement.diskmonitor.entity.TApplicationInfo;
import com.tieto.systemmanagement.diskmonitor.entity.TSWInfo;
import com.tieto.systemmanagement.diskmonitor.entity.ThumbnailInfo;
import com.tieto.systemmanagement.diskmonitor.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangbo on 4/3/15.
 */
public class DiskData {

    private static final long KILO_BYTE = 1024;
    private static final long BYTES_2_MEGES = 1048576L;

    private long internalTotal = 0;
    private long externalTotal = 0;

    private static DiskData mInstance = new DiskData();

    //TODO: public functions
    public static DiskData getInstance() {
        return mInstance;
    }

    public long getStorageTotal() {
        return internalTotal + externalTotal;
    }

    public long getStorageUsed() {
        StatFs internalStatFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long internalFree;

        StatFs externalStatFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long externalFree;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            internalTotal = (internalStatFs.getBlockCountLong() * internalStatFs.getBlockSizeLong()) / (KILO_BYTE * KILO_BYTE * KILO_BYTE);
            internalFree = (internalStatFs.getAvailableBlocksLong() * internalStatFs.getBlockSizeLong()) / (KILO_BYTE * KILO_BYTE * KILO_BYTE);
            externalTotal = (externalStatFs.getBlockCountLong() * externalStatFs.getBlockSizeLong()) / (KILO_BYTE * KILO_BYTE * KILO_BYTE);
            externalFree = (externalStatFs.getAvailableBlocksLong() * externalStatFs.getBlockSizeLong()) / (KILO_BYTE * KILO_BYTE * KILO_BYTE);
        } else {
            internalTotal = ((long) internalStatFs.getBlockCount() * (long) internalStatFs.getBlockSize()) / (KILO_BYTE * KILO_BYTE * KILO_BYTE);
            internalFree = ((long) internalStatFs.getAvailableBlocks() * (long) internalStatFs.getBlockSize()) / (KILO_BYTE * KILO_BYTE * KILO_BYTE);
            externalTotal = ((long) externalStatFs.getBlockCount() * (long) externalStatFs.getBlockSize()) / (KILO_BYTE * KILO_BYTE * KILO_BYTE);
            externalFree = ((long) externalStatFs.getAvailableBlocks() * (long) externalStatFs.getBlockSize()) / (KILO_BYTE * KILO_BYTE * KILO_BYTE);
        }

        return internalTotal + externalTotal - (internalFree + externalFree);
    }

    public long getMemPercentUsed() {
        return 100 * (getStorageUsed()) / getStorageTotal();
    }

    public List<StorageInfo> getStorageSpaceInfos() {
        ArrayList<StorageInfo> spaceInfos = new ArrayList<StorageInfo>();

        StorageInfo info1 = new StorageInfo();
        info1.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_apk));
        info1.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_1));
        info1.setTotal(displaySize(getInstalledAppSize(true)));
        spaceInfos.add(info1);

        StorageInfo info2 = new StorageInfo();
        info2.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_audio));
        info2.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_2));
        info2.setTotal(displaySize(getAlbumSize()));
        spaceInfos.add(info2);

        StorageInfo info3 = new StorageInfo();
        info3.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_zip));
        info3.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_3));
        info3.setTotal(displaySize(getAudioSize()));
        spaceInfos.add(info3);

        return spaceInfos;
    }

    public List<StorageInfo> getSystemSpaceInfos() {
        ArrayList<StorageInfo> systemInfos = new ArrayList<StorageInfo>();

        StorageInfo info1 = new StorageInfo();
        info1.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_media_uninstall));
        info1.setTitle(TApp.getInstance().getString(R.string.disk_space_system_title_1));
        info1.setTotal("");
        systemInfos.add(info1);

        return systemInfos;
    }

    public List<ThumbnailInfo> getThumbnailData() {
        String path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).getAbsolutePath();
        List<ThumbnailInfo> infos = getThumbnailDataWhere(path);
        return infos;
    }

    private List<ThumbnailInfo> getThumbnailDataWhere(String where) {
        File f = new File(where);
        File photos[];
        List<ThumbnailInfo> infos = new ArrayList<ThumbnailInfo>();
        photos = f.listFiles();

        try {
            for (int i = 0; i < photos.length; i++) {
                File file = photos[i];
                if (file.isDirectory()) {
                    String path = file.getAbsolutePath();
                    infos.addAll(getThumbnailDataWhere(path));
                } else if(file.getAbsolutePath().toLowerCase().contains(".jpg")){
                    ThumbnailInfo info = new ThumbnailInfo();
                    info.mItemName = file.getName();
                    info.mItemPath = file.getAbsolutePath();
                    info.mItemIcon = FileUtils.getBitmap(file.getAbsolutePath(),true);
                    infos.add(info);
                }
            }
        }
        catch (Exception e) {

        }

        return infos;
    }

    //get the song list from sd-card
    public List<AudioInfo> getAudioData() {
        List<AudioInfo> audioList = new ArrayList<AudioInfo>();
        audioList.addAll(getWhereAudioData(Environment.DIRECTORY_RINGTONES));
        audioList.addAll(getWhereAudioData(Environment.DIRECTORY_MUSIC));
        return audioList;
    }

    public ArrayList<TSWInfo> getUserInstalledSW() {
        ArrayList<TSWInfo> apps = getInstalledSW(false); /* false = no system packages */
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    public ArrayList<TSWInfo> getPreInstalledSW() {
        ArrayList<TSWInfo> apps = getInstalledSW(true); /* false = no system packages */
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    public ArrayList<TApplicationInfo> getApps(boolean isInstalled) {
        ArrayList<TApplicationInfo> apps = getInstalledApps(isInstalled);
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    public long getInstalledAppSize(boolean getSysPackages) {
        List<PackageInfo> packs = TApp.getInstance().getPackageManager().getInstalledPackages(0);

        long total = 0;
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue;
            }

            File file = new File(p.applicationInfo.sourceDir);
            long sizeInByte = file.length();
            total += sizeInByte;
        }
        return total;
    }

    //TODO: private functions
    private DiskData() {
        getStorageUsed();
    }

    private long getAudioSize() {
        long total = 0;
        String path;

        if (FileUtils.isExternalStorageReadable()) {
            path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_RINGTONES).getAbsolutePath();
            total += FileUtils.getSize(new File(path));

            path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC).getAbsolutePath();
            total += FileUtils.getSize(new File(path));
        }

        return total;
    }

    public String displaySize(long size) {
        if (size > BYTES_2_MEGES) {
            size /= BYTES_2_MEGES;
            return size + "M";
        } else if (size > KILO_BYTE) {
            size /= KILO_BYTE;
            return size + "K";
        }
        return size + "B";
    }

    private long getAlbumSize() {
        long total = 0;

        if (FileUtils.isExternalStorageReadable()) {
            String path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).getAbsolutePath();
            total = FileUtils.getSize(new File(path));
        }

        return total;
    }

    private List<AudioInfo> getWhereAudioData(String where) {
        List<AudioInfo> audioList = new ArrayList<AudioInfo>();
        String path = Environment.getExternalStoragePublicDirectory(
                where).getAbsolutePath();
        File home = new File(path);
        if (home.listFiles().length > 0) {
            for (File file : home.listFiles()) {
                AudioInfo info = new AudioInfo();
                info.mFileName = file.getName();
                info.mItemPath = file.getAbsolutePath();
                info.mSizeDisplay = displaySize(FileUtils.getSize(file));
                audioList.add(info);
            }
        }
        return audioList;
    }

    private ArrayList<TSWInfo> getInstalledSW(boolean isSysPackages) {
        ArrayList<TSWInfo> res = new ArrayList<TSWInfo>();
        List<PackageInfo> packs = TApp.getInstance().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!isSysPackages) && isSystemApp(p.applicationInfo)) {
                continue;
            }

            TSWInfo newInfo = new TSWInfo();
            newInfo.mAppName = p.applicationInfo.loadLabel(TApp.getInstance().getPackageManager()).toString();
            newInfo.mName = p.packageName;
            newInfo.mVersionName = p.versionName;
            newInfo.mVersionCode = p.versionCode;
            newInfo.mPath = p.applicationInfo.sourceDir;
            newInfo.icon = p.applicationInfo.loadIcon(TApp.getInstance().getPackageManager());
            File file = new File(newInfo.mPath);
            long sizeinByte = file.length();
            newInfo.mSize = sizeinByte;
            res.add(newInfo);
        }
        return res;
    }

    private ArrayList<TApplicationInfo> getInstalledApps(boolean isInstalled) {
        ArrayList<TApplicationInfo> res = new ArrayList<TApplicationInfo>();
        List<ApplicationInfo> packs;

        if(isInstalled) {
           packs = TApp.getInstance().getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        }
        else {
           packs = TApp.getInstance().getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        }

        for (int i = 0; i < packs.size(); i++) {
            ApplicationInfo p = packs.get(i);

            File file = new File(p.sourceDir);
            if(!isSystemApp(p)) {
                long sizeByte = file.length();
                TApplicationInfo newInfo = new TApplicationInfo();
                newInfo.mAppName = p.sourceDir;
                newInfo.mName = p.packageName;
                newInfo.mPath = p.sourceDir;
                newInfo.icon = p.loadIcon(TApp.getInstance().getPackageManager());
                newInfo.mSize = sizeByte;
                res.add(newInfo);
            }

        }
        return res;
    }

    private boolean isSystemApp(ApplicationInfo appInfo) {
        return ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }
}
