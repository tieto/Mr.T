package com.tieto.systemmanagement.app.tools;

import android.os.Handler;
import android.os.Message;

import com.tieto.systemmanagement.app.constants.AppListCache;
import com.tieto.systemmanagement.app.model.AppListItemModel;
import com.tieto.systemmanagement.app.model.AppSizeModel;

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

        public void onPackageIconChanged();

        public void onPackageSizeChanged(AppSizeModel appSizeModel);
    }

    class MainHandler extends Handler {
        static final int MSG_PACKAGE_ICON_CHANGED = 1;
        static final int MSG_PACKAGE_SIZE_CHANGED = 2;
        static final int MSG_RUNNING_STATE_CHANGED = 3;

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_PACKAGE_ICON_CHANGED: {
                    for (int i = 0; i < mActiveCallbacks.size(); i++) {
                        mActiveCallbacks.get(i).onPackageIconChanged();
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
                    for (int i = 0; i < mActiveCallbacks.size(); i++) {
                        mActiveCallbacks.get(i).onRunningStateChanged(
                                msg.arg1 != 0);
                    }
                }
                break;
            }
        }
    }

    final MainHandler mMainHandler = new MainHandler();

    public synchronized void sizeChanged(AppSizeModel appSizeModel) {
        AppListItemModel appListItemModel = AppListCache.AppListItemModelCache.get(appSizeModel.getPackageName());
        if (appListItemModel != null) {
            appListItemModel.setSizeInfo(appSizeModel);
        }
        Message msg = mMainHandler.obtainMessage(MainHandler.MSG_PACKAGE_SIZE_CHANGED, appSizeModel);
        mMainHandler.sendMessage(msg);
    }
}
