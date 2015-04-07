package com.tieto.systemmanagement;

import android.app.Application;

/**
 * Created by wangbo on 4/7/15.
 */
public class TApp extends Application {
    private static TApp ourInstance = new TApp();

    public static TApp getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }
}
