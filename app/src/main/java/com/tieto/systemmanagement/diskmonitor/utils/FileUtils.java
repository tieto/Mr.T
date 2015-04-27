package com.tieto.systemmanagement.diskmonitor.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.tieto.systemmanagement.TApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

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

    public static String[] getItemSelected(List<Integer> itemsChecked, List<String> paths) {
        List<Integer> itemsSelected = itemsChecked;
        List<String> arrPaths = paths;

        int len = itemsSelected.size();
        int cnt = 0;
        ArrayList<String> itemsPathSelected = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            if (Utils.int2Bool(itemsSelected.get(i))) {
                cnt++;
                itemsPathSelected.add(arrPaths.get(i));
            }
        }

        if (cnt == 0) {
            DebugToast.debugToast("",
                    "Please select at least one item");
            new SweetAlertDialog(TApp.getInstance(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("没有要删除的东西？")
                    .setCancelText("知道啦")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();
        } else {
            DebugToast.debugToast("",
                    "You've selected Total " + cnt + "----" +
                            itemsPathSelected + " item(s).");

        }

        String[] results = new String[itemsPathSelected.size()];
        for (int i = 0; i < itemsPathSelected.size(); i++) {
            results[i] = itemsPathSelected.get(i);
        }
        return results;
    }

    /**
     * InputStream to byte
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();

        return data;
    }

    /**
     * Byte to bitmap
     *
     * @param bytes
     * @param opts
     * @return
     */
    public static Bitmap getBitmapFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null) {
            if (opts != null) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            } else {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }

        return null;
    }

    public static Bitmap getBitmap(String path, boolean isLargeFlg) {

        //先解析图片边框的大小
        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(path, ops);
        ops.inSampleSize = 1;
        int oHeight = ops.outHeight;
        int oWidth = ops.outWidth;

        //控制压缩比
        int contentHeight = 0;
        int contentWidth = 0;
        if (isLargeFlg) {
            contentHeight = (int) (ops.outHeight * 0.45);
            contentWidth = (int) (ops.outWidth * 0.45);
        } else {
            contentHeight = 250;
            contentHeight = 200;
        }
        if (((float) oHeight / contentHeight) < ((float) oWidth / contentWidth)) {
            ops.inSampleSize = (int) Math.ceil((float) oWidth / contentWidth);
        } else {
            ops.inSampleSize = (int) Math.ceil((float) oHeight / contentHeight);
        }
        ops.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, ops);
        return bm;
    }
}
