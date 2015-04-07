package com.tieto.systemmanagement.authority.entity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.tieto.systemmanagement.R;

import org.androidannotations.annotations.App;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang Ping
 */
public final class AppInfo implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bitmap bitmap = ((BitmapDrawable) mIcon).getBitmap();
        parcel.writeParcelable(bitmap, i);
        parcel.writeString(mName);
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel parcel) {
            Bitmap icon = parcel.readParcelable(Bitmap.class.getClassLoader());
            String label = parcel.readString();
            AppInfo info = new AppInfo(label, new BitmapDrawable(icon));
            return info;
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

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
