package com.tieto.systemmanagement;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

//TODO: change the name
public class FunEntity {
    private int mTitle;
    private int mIcon;
    private Class< ? extends Activity> mActivity;

    public FunEntity(@StringRes int title, @DrawableRes int icon,
                     Class<? extends Activity> activity) {
        mTitle = title;
        mIcon = icon;
        mActivity = activity;
    }

    public int getTitleRes() {
        return mTitle;
    }

    public int getIconRes() {
        return mIcon;
    }

    public Class<? extends Activity> getActivity() {
        return mActivity;
    }
}
