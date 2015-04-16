package com.tieto.systemmanagement.app.adapters;

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
import com.tieto.systemmanagement.app.models.AppInfoModel;
import com.tieto.systemmanagement.app.models.AppSizeModel;
import com.tieto.systemmanagement.app.utils.AppListFragmentTool;
import com.tieto.systemmanagement.app.models.ApplicationsState;
import com.tieto.systemmanagement.app.fragments.AppListFragment;
import com.tieto.systemmanagement.app.utils.constants.AppListCache;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by jinpei on 26/03/15.
 */
public class AppListAdapter extends BaseAdapter {

    private List<AppInfoModel> mAppList;
    private WeakHashMap<Integer, View> mAppViewMap;
    private WeakReference<AppListFragment> mAppListFragment;

    private AppSizeInfoSetterHandler mAppSizeInfoSetterHandler;

    public AppListAdapter(List<AppInfoModel> appList, WeakReference<AppListFragment> appListFragment) {
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

        AppInfoModel mAppInfoModel = mAppList.get(position);

        holder.image.setImageDrawable(mAppInfoModel.getIcon());
        holder.textView_1.setText(mAppInfoModel.getAppLabel());
        holder.textView_2.setText(String.valueOf(mAppInfoModel.getTotalSize()));
        holder.textView_3.setText(String.valueOf(mAppInfoModel.getFirstInstallTime()));
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

    /**
     * Remove item which has been deleted.
     */
    public void removeItem(String packageName) {
        mAppList.remove(AppListCache.AppListItemModelCache.get(packageName));
        this.notifyDataSetChanged();
    }

    final class AppListItemViewHolder {
        public ImageView image;
        public TextView textView_1;
        public TextView textView_2;
        public TextView textView_3;
        public int position;
    }

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

            AppInfoModel appInfoModel = appListAdapter.mAppList.get(msg.arg1);
            appInfoModel.setSizeInfo(appSizeModel);

            View item = appListAdapter.mAppViewMap.get(Integer.valueOf(msg.arg1));
            if (item != null) {
                TextView size = (TextView) item.findViewById(R.id.app_size);
                size.setText(String.valueOf(appInfoModel.getTotalSize()));
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

                AppInfoModel mAppInfoModel = mAppListAdapter.get().mAppList.get(i);

                AppListFragmentTool appListFragmentTool = mAppListFragment.get().getAppInfoTool();
                try {
                    appListFragmentTool.getAppSizeModel(mAppInfoModel.getPackageName(), i, mAppSizeInfoSetterHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class AppDetailIntent extends Intent {

        public static final String APP_PACKAGE_NAME = "app_package_name";

        public AppDetailIntent(int position) {
            AppInfoModel appInfoModel = mAppList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString(APP_PACKAGE_NAME, appInfoModel.getPackageName());
            this.putExtras(bundle);
        }
    }
}
