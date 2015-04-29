package com.tieto.systemmanagement.diskmonitor.utils;

import android.content.pm.ApplicationInfo;
import android.widget.Toast;

import com.tieto.systemmanagement.SystemManagementActivity;
import com.tieto.systemmanagement.TApp;

/**
 * This debug class for debug purpose
 *
 * @author bo.b.wang@tieto.com
 *         Apr 21, 2014 10:42:44 PM
 */
public class DebugToast {
    /**
     * Show logs and debug toasts if ON == true
     */
    public static boolean ON = (TApp.getInstance().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

    public static void debugToast(final String tag, final String message) {
        if (DebugToast.ON) {
            SystemManagementActivity.getContext().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TApp.getInstance(), tag + " --- " + message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void releaseToast(final String tag, final String message) {
        SystemManagementActivity.getContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TApp.getInstance(), tag + "" + message, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
