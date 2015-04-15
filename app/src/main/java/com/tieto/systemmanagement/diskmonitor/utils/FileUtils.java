package com.tieto.systemmanagement.diskmonitor.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by wangbo on 4/9/15.
 */
public class FileUtils {
    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static long getSize(File f) {
       if(!(f.exists())) return 0;

       long size = 0;
        if (f.isDirectory()) {

            for (File file : f.listFiles()) {
                size += getSize(file);
            }
        } else {
            size=f.length();
        }
        return size;
    }
}
