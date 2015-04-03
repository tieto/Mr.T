package com.tieto.systemmanagement.notifications;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.tieto.systemmanagement.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gujiao on 02/04/15.
 */
public class NotifactionManagerAdapter extends BaseAdapter {

    private List<ApplicationInfo> mApps;

    private Context mContext;
    private LayoutInflater mInflater;
    private PackageManager pm;
    private AppOpsManager appOpsManager;
    private Class[] args = new Class[] {
            Integer.TYPE,
            Integer.TYPE,
            String.class,
            Integer.TYPE
    };
    private Class[] argsForOps = new Class[] {
            int[].class
    };

    private int[] ops = new int[] {
            11
    };
    Map<String, Integer> modes = new HashMap<String, Integer>();

    public NotifactionManagerAdapter(Context context, List<ApplicationInfo> apps) {
        this.mApps = apps;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.pm = this.mContext.getPackageManager();
        appOpsManager = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
        initMode();
        getMode(ops);

    }

    @Override
    public int getCount() {
        return null == mApps ? 0 : mApps.size();
    }
    @Override
    public Object getItem(int position) {
        return null == mApps ? null : mApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.notification_list_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.notification_app_icon);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.notification_app_name);
            viewHolder.aSwitch = (Switch) convertView.findViewById(R.id.notification_notify_switch);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ApplicationInfo info = mApps.get(position);
        viewHolder.imageView.setImageDrawable(info.loadIcon(pm));
        viewHolder.textView.setText(info.loadLabel(pm));
        bindView(viewHolder, position);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
        Switch aSwitch;
    }
    private void bindView(ViewHolder viewHolder, int position) {
        final ApplicationInfo info = mApps.get(position);
        viewHolder.imageView.setImageDrawable(info.loadIcon(pm));
        viewHolder.textView.setText(info.loadLabel(pm));
        Integer mode = modes.get(info.packageName);
        viewHolder.aSwitch.setOnCheckedChangeListener(new SwitchChangeListener(info));
        viewHolder.aSwitch.setChecked(mode == AppOpsManager.MODE_IGNORED);
    }


    private class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

        private ApplicationInfo appInfo;

        public SwitchChangeListener(ApplicationInfo appInfo) {
            this.appInfo = appInfo;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.notification_notify_switch:
                    banNotification(appInfo, isChecked);
                    break;
                default:
                    break;
            }
        }
    }

    private void banNotification(ApplicationInfo appInfo, boolean isChecked) {
        Object[] params = new Object[] {
                11,
                appInfo.uid,
                appInfo.packageName,
                isChecked ? AppOpsManager.MODE_IGNORED : AppOpsManager.MODE_ALLOWED
        };
        try {
            Method setMode = appOpsManager.getClass().getMethod("setMode", args);
            setMode.invoke(appOpsManager, params);
            modes.put(appInfo.packageName, isChecked ? AppOpsManager.MODE_IGNORED : AppOpsManager.MODE_ALLOWED);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void getMode(int[] ops) {
        Object[] params = new Object[] {
               ops
        };
        try {
            Method getOpsForPackage = appOpsManager.getClass().getMethod("getPackagesForOps", argsForOps);
            List packageOps = (List) getOpsForPackage.invoke(appOpsManager, params);
            if (packageOps != null && packageOps.size() > 0) {
                for (int i = 0; i < packageOps.size(); i ++) {
                    Object packageOp = packageOps.get(i);
                    Method getOps = packageOp.getClass().getMethod("getOps");
                    List opsEntry = (List) getOps.invoke(packageOp);
                    if (opsEntry.size() == 1) {
                        Object entry = opsEntry.get(0);
                        Method getMode = entry.getClass().getMethod("getMode");
                        int mode = (int) getMode.invoke(entry);
                        modes.put(String.valueOf(packageOp.getClass().getMethod("getPackageName").invoke(packageOp)), mode);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMode() {
        for (int i = 0; i < mApps.size(); i ++) {
            modes.put(mApps.get(i).packageName, AppOpsManager.MODE_ALLOWED);
        }
    }
}
