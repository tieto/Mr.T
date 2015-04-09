package com.tieto.systemmanagement.authority.entity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.util.Log;

import com.tieto.systemmanagement.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang Ping
 */
public final class AppWrapper implements Parcelable {

    private ApplicationInfo mAppInfo;

    public AppWrapper(ApplicationInfo appInfo) {
        if (appInfo == null) {
            throw new NullPointerException("Attempt to access the null object of appInfo.");
        }
        mAppInfo = appInfo;
    }

    public Drawable loadIcon(Context context) {
        return mAppInfo.loadIcon(context.getPackageManager());
    }

    public String getName(Context context) {
        return mAppInfo.loadLabel(context.getPackageManager()).toString();
    }

    public int getUid() {
        return mAppInfo.uid;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AppWrapper)) {
            return false;
        }
        return mAppInfo.uid == ((AppWrapper)o).getUid();
    }

    public int getPermissionCount() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mAppInfo, i);
    }

    public static final Creator<AppWrapper> CREATOR = new Creator<AppWrapper>() {
        @Override
        public AppWrapper createFromParcel(Parcel parcel) {
            ApplicationInfo app = parcel.readParcelable(ApplicationInfo.class.getClassLoader());
            AppWrapper info = new AppWrapper(app);
            return info;
        }

        @Override
        public AppWrapper[] newArray(int size) {
            return new AppWrapper[size];
        }
    };

    public static List<AppWrapper> getApplicationList(Context context, boolean hasSystem) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfo = pm.getInstalledPackages(
                PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        List<AppWrapper> list = new ArrayList<AppWrapper>();
        for (final PackageInfo pkg : packageInfo) {
            ApplicationInfo app = pkg.applicationInfo;

            // Ignore the system app
            if (!hasSystem && (app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }

            if (app != null) {
                list.add(new AppWrapper(app));
            }
        }
        return list;
    }

    private static AppPermission loadPermission(Context context,
                PackageManager pm, String permission) {
        try {
            PermissionInfo permissionInfo =
                    pm.getPermissionInfo(permission, PackageManager.GET_META_DATA);
            int level = permissionInfo.protectionLevel;
            if (level == PermissionInfo.PROTECTION_DANGEROUS) {
                int labelRes = permissionInfo.labelRes == 0?
                        R.string.auth_unknown_permission : permissionInfo.labelRes;
                String labelString = "";
                try {
                    labelString = context.getString(labelRes);
                } catch (Exception e) {
                    Log.e("AppInfo", e.toString());
                }
                // Currently left the description as empty
                return new AppPermission(labelString, "");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AppInfo", e.toString());
        }
        return null;
    }
}
