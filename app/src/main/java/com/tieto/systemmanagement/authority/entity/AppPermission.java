package com.tieto.systemmanagement.authority.entity;

import android.content.pm.ApplicationInfo;

/**
 * @author Jiang Ping
 */
public final class AppPermission {

    public  int op;
    public  int mode;
    public String label;
    private String description;
    public ApplicationInfo applicationInfo;


    public AppPermission(String name, int aOp, int aMode,ApplicationInfo aApplicationInfo) {
        label = name;
        op = aOp;
        mode = aMode;
        applicationInfo = aApplicationInfo;
    }

}
