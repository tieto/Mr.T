package com.tieto.systemmanagement.authority.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.authority.entity.AppInfo;

import java.util.List;

/**
 * @author Jiang Ping
 */
public class AppInfoAdapter extends BaseAdapter {

    private List<AppInfo> mData;
    private LayoutInflater mInflater;

    private static String FORMATTER_HTML = null;

    public AppInfoAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        FORMATTER_HTML = context.getString(R.string.auth_permission_count_format);
    }

    public void setAppListData(List<AppInfo> apps) {
        mData = apps;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null? 0 : mData.size();
    }

    @Override
    public AppInfo getItem(int pos) {
        return mData == null? null : mData.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        ConvertHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_item_auth_appinfo, viewGroup, false);
            holder = new ConvertHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.name = (TextView) convertView.findViewById(R.id.app_name);
            holder.permissionCount = (TextView) convertView.findViewById(R.id.permission_count);
            convertView.setTag(holder);
        } else {
            holder = (ConvertHolder) convertView.getTag();
        }
        AppInfo info = mData.get(pos);
        holder.icon.setImageDrawable(info.getIcon());
        holder.name.setText(info.getName());
        holder.permissionCount.setText(Html.fromHtml(
                String.format(FORMATTER_HTML, info.getPermissionCount())));
        return convertView;
    }

    private static final class ConvertHolder {
        private ImageView icon;
        private TextView name;
        private TextView permissionCount;
    }
}
