package com.tieto.systemmanagement;

import android.app.Activity;
import android.graphics.drawable.Drawable;

/**
 * Created by gujiao on 24/03/15.
 */
public class FunEntity {
    private CharSequence title;
    private Drawable icon;
    private Class< ? extends Activity> activityclass;

    public FunEntity(String title, Drawable icon, Class<? extends Activity> activityclass) {
        this.title = title;
        this.icon = icon;
        this.activityclass = activityclass;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Class<? extends Activity> getActivityclass() {
        return activityclass;
    }

    public void setActivityclass(Class<? extends Activity> activityclass) {
        this.activityclass = activityclass;
    }

    @Override
    public String toString() {
        return title.toString();
    }
}
