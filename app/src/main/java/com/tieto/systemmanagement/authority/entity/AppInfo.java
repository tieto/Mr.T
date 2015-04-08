package com.tieto.systemmanagement.authority.entity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.os.Process;
import android.util.Log;

import com.tieto.systemmanagement.R;

import org.androidannotations.annotations.App;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang Ping
 */
public final class AppInfo implements Parcelable {

    private Bitmap mIcon;
    private String mName;
    private List<AppPermission> mPermissions = new ArrayList<AppPermission>();

    public AppInfo(String name, Bitmap icon) {
        mName = name;
        mIcon = icon;
    }

    public Bitmap getIcon() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mIcon, i);
        parcel.writeString(mName);
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel parcel) {
            Bitmap icon = parcel.readParcelable(Bitmap.class.getClassLoader());
            String label = parcel.readString();
            AppInfo info = new AppInfo(label, icon);
            return info;
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    public static List<AppInfo> getApplicationList(Context context, boolean hasSystem) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfo = pm.getInstalledPackages(
                PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        List<AppInfo> list = new ArrayList<AppInfo>();
        for (final PackageInfo pkg : packageInfo) {
            ApplicationInfo app = pkg.applicationInfo;

            // Ignore the system app
            if (!hasSystem && (app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }

            // Load app icon and name
            String label = app.loadLabel(pm).toString();
            Drawable iconDrawable = app.loadIcon(pm);
            Bitmap icon = BitmapUtils.convertDrawableToBitmap(iconDrawable);
            AppInfo item = new AppInfo(label, icon);

            // Load app permissions
            String[] permissions = pkg.requestedPermissions;
            if (permissions != null && permissions.length > 0) {
                for (String per : permissions) {
                    AppPermission ap = loadPermission(context, pm, per);
                    if (ap != null) {
                        item.addPermission(ap);
                    }
                }
            }

            list.add(item);
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
