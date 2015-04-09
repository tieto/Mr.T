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

    private PackageInfo mPackageInfo;
    private ApplicationInfo mAppInfo;

    public AppWrapper(PackageInfo packageInfo) {
        if (packageInfo == null) {
            throw new NullPointerException("Attempt to access the null object of packageInfo.");
        }
        mPackageInfo = packageInfo;
        mAppInfo = packageInfo.applicationInfo;
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
        return mPackageInfo.permissions == null? 0 : mPackageInfo.permissions.length;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mPackageInfo, i);
    }

    public static final Creator<AppWrapper> CREATOR = new Creator<AppWrapper>() {
        @Override
        public AppWrapper createFromParcel(Parcel parcel) {
            PackageInfo app = parcel.readParcelable(PackageInfo.class.getClassLoader());
            AppWrapper info = new AppWrapper(app);
            return info;
        }

        @Override
        public AppWrapper[] newArray(int size) {
            return new AppWrapper[size];
        }
    };
}
