package com.tieto.systemmanagement.diskmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.entity.StorageInfo;

import java.util.List;

/**
 * Created by wangbo on 4/3/15.
 */
public class DiskStorageAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<StorageInfo> mSpaceArray = null;
    private static LayoutInflater mInflater = null;

    public DiskStorageAdapter(Context context, List<StorageInfo> list) {
        mContext = context;
        mSpaceArray = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mSpaceArray.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null)
            v = mInflater.inflate(R.layout.item_disk_space, null);

        StorageInfo info = mSpaceArray.get(position);
        ImageView iv = (ImageView)v.findViewById(R.id.icon);
        iv.setImageDrawable(info.getIcon());

        TextView tv = (TextView)v.findViewById(R.id.title);
        tv.setText(info.getTitle());

        TextView summary = (TextView)v.findViewById(R.id.summary);
        summary.setText(info.getTotal());
        return v;
    }
}
