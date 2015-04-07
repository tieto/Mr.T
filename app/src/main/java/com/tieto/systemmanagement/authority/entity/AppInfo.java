package com.tieto.systemmanagement.authority.entity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.tieto.systemmanagement.R;

import org.androidannotations.annotations.App;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang Ping
 */
public final class AppInfo {

    private Drawable mIcon;
    private String mName;
    private List<AppPermission> mPermissions = new ArrayList<AppPermission>();

    public AppInfo(String name, Drawable icon) {
        mName = name;
        mIcon = icon;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public String getName() {
        return mName;
    }

    public int getPermissionCount() {
        return mPermissions.size();
    }

    public void addPermission(AppPermission permission) {
        mPermissions.add(permission);
    }

    public static List<AppInfo> getApplicationList(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfo = pm.getInstalledPackages(
                PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        List<AppInfo> list = new ArrayList<AppInfo>();
        for (final PackageInfo pkg : packageInfo) {
            ApplicationInfo app = pkg.applicationInfo;
            AppInfo item = new AppInfo(app.loadLabel(pm).toString(), app.loadIcon(pm));

            String[] permissions = pkg.requestedPermissions;
            if (permissions != null && permissions.length > 0) {
                for (String per : permissions) {
                    try {
                        PermissionInfo permissionInfo =
                                pm.getPermissionInfo(per, PackageManager.GET_META_DATA);
                        int level = permissionInfo.protectionLevel;
                        if (level == PermissionInfo.PROTECTION_DANGEROUS) {
                            int label = permissionInfo.labelRes == 0?
                                    R.string.auth_unknown_permission : permissionInfo.labelRes;
                            String labelString = "";
                            try {
                                labelString = context.getString(label);
                            } catch (Exception e) {
                                Log.e("AppInfo", e.toString());
                            }
                            item.addPermission(new AppPermission(labelString, ""));
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            list.add(item);
        }
        return list;
    }
}
