package com.tieto.systemmanagement.diskmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.entity.SpaceInfo;

import java.util.List;

/**
 * Created by wangbo on 4/3/15.
 */
public class DiskSpaceAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<SpaceInfo> mSpaceArray = null;
    private static LayoutInflater mInflater = null;

    public DiskSpaceAdapter(Context context, List<SpaceInfo> list) {
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

        return v;
    }
}
