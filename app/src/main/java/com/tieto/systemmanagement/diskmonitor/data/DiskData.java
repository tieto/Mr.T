package com.tieto.systemmanagement.diskmonitor.data;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.diskmonitor.entity.StorageInfo;
import com.tieto.systemmanagement.diskmonitor.entity.ThumbNailInfo;
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

    private long internalTotal = 0 ;
    private long externalTotal = 0;

    private static DiskData mInstance = new DiskData();

    //TODO: public functions
    public static DiskData getInstance() {
        return mInstance;
    }

    public long getStorageTotal() {
        return  internalTotal + externalTotal;
    }

    public long getStorageUsed() {
        StatFs internalStatFs = new StatFs( Environment.getRootDirectory().getAbsolutePath() );
        long internalFree;

        StatFs externalStatFs = new StatFs( Environment.getExternalStorageDirectory().getAbsolutePath() );
        long externalFree;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            internalTotal = ( internalStatFs.getBlockCountLong() * internalStatFs.getBlockSizeLong() ) / ( KILO_BYTE * KILO_BYTE * KILO_BYTE);
            internalFree = ( internalStatFs.getAvailableBlocksLong() * internalStatFs.getBlockSizeLong() ) / ( KILO_BYTE * KILO_BYTE * KILO_BYTE);
            externalTotal = ( externalStatFs.getBlockCountLong() * externalStatFs.getBlockSizeLong() ) / ( KILO_BYTE * KILO_BYTE * KILO_BYTE);
            externalFree = ( externalStatFs.getAvailableBlocksLong() * externalStatFs.getBlockSizeLong() ) / ( KILO_BYTE * KILO_BYTE * KILO_BYTE);
        }
        else {
            internalTotal = ( (long) internalStatFs.getBlockCount() * (long) internalStatFs.getBlockSize() ) / ( KILO_BYTE * KILO_BYTE * KILO_BYTE);
            internalFree = ( (long) internalStatFs.getAvailableBlocks() * (long) internalStatFs.getBlockSize() ) / ( KILO_BYTE * KILO_BYTE * KILO_BYTE);
            externalTotal = ( (long) externalStatFs.getBlockCount() * (long) externalStatFs.getBlockSize() ) / ( KILO_BYTE * KILO_BYTE * KILO_BYTE);
            externalFree = ( (long) externalStatFs.getAvailableBlocks() * (long) externalStatFs.getBlockSize() ) / ( KILO_BYTE * KILO_BYTE * KILO_BYTE);
        }

        return internalTotal + externalTotal -(internalFree + externalFree);
    }

    public long getMemPercentUsed() {
        return 100*(getStorageUsed())/getStorageTotal();
    }

    public List<StorageInfo> getStorageSpaceInfos() {
        ArrayList<StorageInfo> spaceInfos = new ArrayList<StorageInfo>();

        StorageInfo info1= new StorageInfo();
        info1.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_apk));
        info1.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_1));
        info1.setTotal("0M");
        spaceInfos.add(info1);

        StorageInfo info2= new StorageInfo();
        info2.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_audio));
        info2.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_2));
        info2.setTotal(displaySize(getAlbumSize()));
        spaceInfos.add(info2);

        StorageInfo info3= new StorageInfo();
        info3.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_file_zip));
        info3.setTitle(TApp.getInstance().getString(R.string.disk_space_store_title_3));
        info3.setTotal(displaySize(getAudioSize()));
        spaceInfos.add(info3);

        return spaceInfos;
    }

    public List<StorageInfo> getSystemSpaceInfos() {
        ArrayList<StorageInfo> systemInfos = new ArrayList<StorageInfo>();

        StorageInfo info1= new StorageInfo();
        info1.setIcon(TApp.getInstance().getDrawable(R.drawable.sysclear_media_uninstall));
        info1.setTitle(TApp.getInstance().getString(R.string.disk_space_system_title_1));
        info1.setTotal("");
        systemInfos.add(info1);

        return systemInfos;
    }

    public ThumbNailInfo getThumbnailData() {
        int count;
        int imageColumnIndex;

        Cursor imageCursor;

        //Specify the columns to read
        String[] columns = {MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Thumbnails._ID,
                MediaStore.Images.Thumbnails.KIND,
                MediaStore.Images.Thumbnails.IMAGE_ID};
        String orderBy = MediaStore.Images.Thumbnails._ID;
        String selection1 = MediaStore.Images.Thumbnails.KIND + "=" + // Select only mini's
                MediaStore.Images.Thumbnails.MINI_KIND;

        imageCursor = TApp.getInstance().getApplicationContext().getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                columns,
                selection1,
                null,
                orderBy);
        imageColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);

        count = imageCursor.getCount();
        ThumbNailInfo info = new ThumbNailInfo(count);

        for (int i = 0; i < count; i++) {
            imageCursor.moveToPosition(i);
            int id = imageCursor.getInt(imageColumnIndex);
            int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            info.mThumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                    TApp.getInstance().getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MINI_KIND, null);
            info.mThumbnailPath[i] = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
            info.mArrPath[i] = imageCursor.getString(dataColumnIndex);
        }

        return info;
    }

    //get the song list from sd-card
    public List<String> getAudioData() {
        List<String> audioList = new ArrayList<String>();
        String path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_RINGTONES).getAbsolutePath();
        File home = new File(path);

        if (home.listFiles().length > 0) {
            for (File file : home.listFiles()) {
                audioList.add(file.getName());
            }
        }
        return audioList;
    }

    //TODO: private functions
    private DiskData() {
        getStorageUsed();
    }

    private long getAudioSize() {
        long total = 0;
        String path;

        if (FileUtils.isExternalStorageReadable()){
            path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_RINGTONES).getAbsolutePath();
            total += FileUtils.getSize(new File(path));

            path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC).getAbsolutePath();
            total +=  FileUtils.getSize(new File(path));
        }

        return total;
    }

    private String displaySize(long size) {
        if (size > BYTES_2_MEGES) {
            size/= BYTES_2_MEGES;
            return  size+"M";
        }
        else if (size > KILO_BYTE) {
            size/= KILO_BYTE;
            return size+"K";
        }
        return size+"B";
    }

   private long getAlbumSize() {
        long total = 0;

        if (FileUtils.isExternalStorageReadable())
        {
            String path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).getAbsolutePath();
            total = FileUtils.getSize(new File(path));
        }

        return total;
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
