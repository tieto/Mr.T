package com.tieto.systemmanagement.app.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.app.model.AppListItemModel;
import com.tieto.systemmanagement.app.model.AppSizeModel;
import com.tieto.systemmanagement.app.tools.AppListFragmentTool;
import com.tieto.systemmanagement.app.tools.ApplicationsState;
import com.tieto.systemmanagement.app.ui.AppListFragment;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by jinpei on 26/03/15.
 */
public class AppListAdapter extends BaseAdapter {

    private List<AppListItemModel> mAppList;
    private WeakHashMap<Integer, View> mAppViewMap;
    private WeakReference<AppListFragment> mAppListFragment;

    private AppSizeInfoSetterHandler mAppSizeInfoSetterHandler;

    public AppListAdapter(List<AppListItemModel> appList, WeakReference<AppListFragment> appListFragment) {
        mAppList = appList;
        mAppListFragment = appListFragment;

        mAppViewMap = new WeakHashMap<Integer, View>();
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AppListFragment appListFragment = mAppListFragment.get();
        if (appListFragment == null) return null;

        AppListItemViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(appListFragment.getActivity());
            convertView = mInflater.inflate(R.layout.app_list_item, null);

            holder = new AppListItemViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.textView_1 = (TextView) convertView.findViewById(R.id.app_name);
            holder.textView_2 = (TextView) convertView.findViewById(R.id.app_size);
            holder.textView_3 = (TextView) convertView.findViewById(R.id.app_date);

            convertView.setTag(holder);
        } else {
            holder = (AppListItemViewHolder) convertView.getTag();
            mAppViewMap.remove(holder.position);
        }

        AppListItemModel mAppListItemModel = mAppList.get(position);

        holder.image.setImageDrawable(mAppListItemModel.getIcon());
        holder.textView_1.setText(mAppListItemModel.getAppLabel());
        holder.textView_2.setText(String.valueOf(mAppListItemModel.getTotalSize()));
        holder.textView_3.setText(String.valueOf(mAppListItemModel.getFirstInstallTime()));
        holder.position = position;

        mAppViewMap.put(Integer.valueOf(position), convertView);

        return convertView;
    }

    /**
     * Start up a thread to get the size info.
     */
    public void beginToGetSize() {
        if (mAppSizeInfoSetterHandler == null && mAppListFragment.get() != null && mAppListFragment.get().isAlive) {
            mAppSizeInfoSetterHandler = new AppSizeInfoSetterHandler(this);
            new Thread(new AppSizeInfoRunnable(this)).start();
        }
    }

    final class AppListItemViewHolder {
        public ImageView image;
        public TextView textView_1;
        public TextView textView_2;
        public TextView textView_3;
        public int position;
    }

//    /**
//     * A thread to set app size info.
//     */
//    private static class AppSizeInfoSetterRunnable implements Runnable {
//
//        /**
//         * Position of item in app list.
//         */
//        private int position;
//
//        /**
//         * Package name of app info.
//         */
//        private String packageName;
//
//        private AppSizeInfoSetterHandler appSizeInfoSetterHandler;
//
//        private WeakReference<AppListAdapter> mAppListAdapter;
//
//        private AppSizeInfoSetterRunnable(int position, String packageName, AppSizeInfoSetterHandler appSizeInfoSetterHandler, WeakReference<AppListAdapter> appListAdapter) {
//            this.position = position;
//            this.packageName = packageName;
//            this.appSizeInfoSetterHandler = appSizeInfoSetterHandler;
//            mAppListAdapter = appListAdapter;
//        }
//
//        @Override
//        public void run() {
//            if(mAppListAdapter.get() == null) return;
//            AppListFragment appListFragment = mAppListAdapter.get().mAppListFragment.get();
//            if (appListFragment == null || !appListFragment.isAlive) return;
//
//            AppListFragmentTool appListFragmentTool = appListFragment.getAppInfoTool();
//            try {
//                appListFragmentTool.getAppSizeModel(packageName, position, appSizeInfoSetterHandler);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static class AppSizeInfoSetterHandler extends Handler {

        private WeakReference<AppListAdapter> adapter;

        public AppSizeInfoSetterHandler(AppListAdapter adapter) {
            this.adapter = new WeakReference<AppListAdapter>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            AppListAdapter appListAdapter = adapter.get();

            if (appListAdapter == null) {
                return;
            }
            AppSizeModel appSizeModel = (AppSizeModel) msg.obj;

            AppListItemModel appListItemModel = appListAdapter.mAppList.get(msg.arg1);
            appListItemModel.setSizeInfo(appSizeModel);

            View item = appListAdapter.mAppViewMap.get(Integer.valueOf(msg.arg1));
            if (item != null) {
                TextView size = (TextView) item.findViewById(R.id.app_size);
                size.setText(String.valueOf(appListItemModel.getTotalSize()));
            }
            ApplicationsState.getInstance().sizeChanged(appSizeModel);
        }
    }

    private class AppSizeInfoRunnable implements Runnable {

        private WeakReference<AppListAdapter> mAppListAdapter;

        private AppSizeInfoRunnable(AppListAdapter appListAdapter) {
            mAppListAdapter = new WeakReference<AppListAdapter>(appListAdapter);
        }

        @Override
        public void run() {
            int size = mAppListAdapter.get().mAppList.size();

            for (int i = 0; i < size; i++) {
                if (mAppListAdapter.get().mAppListFragment.get() == null || !mAppListAdapter.get().mAppListFragment.get().isAlive) {
                    return;
                }

                AppListItemModel mAppListItemModel = mAppListAdapter.get().mAppList.get(i);
//                Thread setter = null;
//                if (!mAppListItemModel.isSizeSet()) {
//                    setter = new Thread(new AppSizeInfoSetterRunnable(i, mAppListItemModel.getPackageName(), mAppListAdapter.get().mAppSizeInfoSetterHandler, mAppListAdapter));
//                    AppListCache.ExecutorService.execute(setter);
//                }

                AppListFragmentTool appListFragmentTool = mAppListFragment.get().getAppInfoTool();
                try {
                    appListFragmentTool.getAppSizeModel(mAppListItemModel.getPackageName(), i, mAppSizeInfoSetterHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class AppDetailIntent extends Intent {

        public static final String APP_PACKAGE_NAME = "app_package_name";

        public AppDetailIntent(int position) {
            AppListItemModel appListItemModel = mAppList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString(APP_PACKAGE_NAME, appListItemModel.getPackageName());
            this.putExtras(bundle);
        }
    }
}
