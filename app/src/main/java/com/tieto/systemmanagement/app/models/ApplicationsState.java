package com.tieto.systemmanagement.app.models;

import android.os.Handler;
import android.os.Message;

import com.tieto.systemmanagement.app.utils.constants.AppListCache;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by jinpei on 07/04/15.
 */
public class ApplicationsState {

    final Map<String, Callbacks> mActiveCallbacks = new WeakHashMap<String, Callbacks>();

    private static ApplicationsState mApplicationsState;

    private ApplicationsState() {

    }

    public synchronized static ApplicationsState getInstance() {
        if (mApplicationsState == null) {
            mApplicationsState = new ApplicationsState();
        }
        return mApplicationsState;
    }

    public synchronized static void setOnStateChanged(String packageName, Callbacks callbacks) {
        if (mApplicationsState == null) {
            mApplicationsState = new ApplicationsState();
        }
        mApplicationsState.mActiveCallbacks.put(packageName, callbacks);
    }

    public static interface Callbacks {
        public void onRunningStateChanged(boolean running);

        public void onPackageStateChanged();

        public void onPackageSizeChanged(AppSizeModel appSizeModel);
    }

    class MainHandler extends Handler {
        static final int MSG_PACKAGE_STATE_CHANGED = 1;
        static final int MSG_PACKAGE_SIZE_CHANGED = 2;
        static final int MSG_RUNNING_STATE_CHANGED = 3;

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_PACKAGE_STATE_CHANGED: {
                    String packageName = (String) msg.obj;
                    Callbacks callback = mActiveCallbacks.get(packageName);
                    if (callback != null) {
                        callback.onPackageStateChanged();
                    }
                }
                break;
                case MSG_PACKAGE_SIZE_CHANGED: {
                    AppSizeModel appSizeModel = (AppSizeModel) msg.obj;
                    Callbacks callback = mActiveCallbacks.get(appSizeModel.getPackageName());
                    if (callback != null) {
                        callback.onPackageSizeChanged(appSizeModel);
                    }
                }
                break;
                case MSG_RUNNING_STATE_CHANGED: {
                    String packageName = (String) msg.obj;
                    Callbacks callback = mActiveCallbacks.get(packageName);
                    if (callback != null) {
                        callback.onRunningStateChanged(false);
                    }
                }
                break;
            }
        }
    }

    final MainHandler mMainHandler = new MainHandler();

    public synchronized void sizeChanged(AppSizeModel appSizeModel) {
        AppInfoModel appInfoModel = AppListCache.AppListItemModelCache.get(appSizeModel.getPackageName());
        if (appInfoModel != null) {
            appInfoModel.setSizeInfo(appSizeModel);
        }
        Message msg = mMainHandler.obtainMessage(MainHandler.MSG_PACKAGE_SIZE_CHANGED, appSizeModel);
        mMainHandler.sendMessage(msg);
    }

    public synchronized void stateChanged(String packageName) {
        Message msg = mMainHandler.obtainMessage(MainHandler.MSG_PACKAGE_STATE_CHANGED,packageName);
        mMainHandler.sendMessage(msg);
    }

    public synchronized void runningStateChanged(String packageName, boolean isRunning) {
        if(isRunning) {
            return;
        }
        Message msg = mMainHandler.obtainMessage(MainHandler.MSG_RUNNING_STATE_CHANGED,packageName);
        mMainHandler.sendMessage(msg);
    }

    public synchronized void removeOnStateChanged(String packageName) {
        mActiveCallbacks.remove(packageName);
    }
}
