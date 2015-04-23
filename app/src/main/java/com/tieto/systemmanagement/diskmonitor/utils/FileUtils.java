package com.tieto.systemmanagement.diskmonitor.utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

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
        if (!(f.exists())) return 0;

        long size = 0;
        if (f.isDirectory()) {

            for (File file : f.listFiles()) {
                size += getSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static String[]  getItemSelected(boolean itemsChecked[], String pathsChecked[]) {
        boolean[] itemsSelected = itemsChecked;
        String[] arrPath = pathsChecked;

        int len = itemsSelected.length;
        int cnt = 0;
        ArrayList<String> itemsPathSelected = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            if (itemsSelected[i]) {
                cnt++;
                itemsPathSelected.add(arrPath[i]);
            }
        }

        if (cnt == 0) {
            DebugToast.debugToast("",
                    "Please select at least one item");
        } else {
            DebugToast.debugToast("",
                    "You've selected Total " + cnt + "----" +
                            itemsPathSelected + " item(s).");

        }

        String[] results = new String[itemsPathSelected.size()];
        for (int i=0; i<itemsPathSelected.size();i++) {
            results[i] = itemsPathSelected.get(i);
        }
        return results;
    }
}
