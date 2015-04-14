package com.tieto.systemmanagement.authority.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.authority.entity.AppWrapper;
import com.tieto.systemmanagement.authority.entity.BitmapWorker;

import java.util.List;

/**
 * @author Jiang Ping
 */
public class AppInfoAdapter extends BaseAdapter {

    private List<AppWrapper> mData;
    private LayoutInflater mInflater;

    private static String FORMATTER_HTML = null;
    private IconLoader mLoader = null;

    public AppInfoAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        FORMATTER_HTML = context.getString(R.string.auth_permission_count_format);
        mLoader = new IconLoader(context);
    }

    public void setAppListData(List<AppWrapper> apps) {
        mData = apps;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null? 0 : mData.size();
    }

    @Override
    public AppWrapper getItem(int pos) {
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
        AppWrapper info = mData.get(pos);
        mLoader.loadImage(holder.icon, info);
        holder.name.setText(info.getName(mInflater.getContext()));
        holder.permissionCount.setText(Html.fromHtml(
                String.format(FORMATTER_HTML, info.getPermissionCount())));
        return convertView;
    }

    private static final class ConvertHolder {
        private ImageView icon;
        private TextView name;
        private TextView permissionCount;
    }

    private static final class IconLoader extends ImageLoader {

        public IconLoader(Context context) {
            super(context);
        }

        @Override
        protected Bitmap onCreateBitmap(Object data) {
            if (data instanceof AppWrapper) {
                AppWrapper app = (AppWrapper)data;
                Drawable drawable = app.loadIcon(mContext);
                BitmapWorker bw = new BitmapWorker(drawable);
                return bw.getBitmap();
            }
            return null;
        }
    }
}
