package com.tieto.systemmanagement.startup;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tieto.systemmanagement.R;

import java.util.List;

/**
 * Created by gujiao on 15/04/15.
 */
public class StartUpAdapter extends BaseAdapter {

    private Context mContext;
    private List<ResolveInfo> mData;
    private LayoutInflater mInflater;
    private PackageManager pm;

    public StartUpAdapter(Context context, List<ResolveInfo> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
        pm = mContext.getPackageManager();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
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
            convertView = mInflater.inflate(R.layout.start_up_list_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.start_up_app_icon);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.start_up_app_name);
            viewHolder.textViewSecondaryName = (TextView) convertView.findViewById(R.id.start_up_secondary_name);
            viewHolder.aSwitch = (CompoundButton) convertView.findViewById(R.id.start_up_notify_switch);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindView(viewHolder, mData.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView textViewSecondaryName;
        CompoundButton aSwitch;
    }

    private void bindView(ViewHolder viewHolder, ResolveInfo info) {
        ApplicationInfo appInfo = info.activityInfo.applicationInfo;
        viewHolder.imageView.setImageDrawable(appInfo.loadIcon(pm));
        viewHolder.textView.setText(appInfo.loadLabel(pm));
        viewHolder.textViewSecondaryName.setText(info.activityInfo.name);
        ComponentName component = new ComponentName(appInfo.packageName, info.activityInfo.name);
        viewHolder.aSwitch.setOnCheckedChangeListener(new SwitchChangeListener(component));
        int state = pm.getComponentEnabledSetting(component);
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            viewHolder.aSwitch.setChecked(true);
        } else {
            viewHolder.aSwitch.setChecked(false);
        }
    }

    private class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

        private ComponentName mComponentName;

        public SwitchChangeListener(ComponentName componentName) {
            mComponentName = componentName;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.start_up_notify_switch:
                    try {
                        if (isChecked) {
                            pm.setComponentEnabledSetting(mComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        } else {
                            pm.setComponentEnabledSetting(mComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, R.string.notification_prohibit_text, Toast.LENGTH_SHORT).show();
                        buttonView.setChecked(!isChecked);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
