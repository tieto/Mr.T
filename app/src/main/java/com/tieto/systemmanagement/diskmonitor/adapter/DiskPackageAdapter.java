package com.tieto.systemmanagement.diskmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.data.DiskData;
import com.tieto.systemmanagement.diskmonitor.entity.ProcessInfo;

import java.util.List;

/**
 * Created by wangbo on 4/3/15.
 */
public class DiskPackageAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<ProcessInfo> mPackages = null;
    private static LayoutInflater mInflater = null;
    Integer count =0;
    private boolean[] mItemsChecked;

    public DiskPackageAdapter(Context context, List<ProcessInfo> list) {
        mContext = context;
        mPackages = list;
        count = mPackages.size();
        mItemsChecked = new boolean[count];
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mPackages.size();
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
            v = mInflater.inflate(R.layout.item_disk_package, null);

        ProcessInfo info = mPackages.get(position);
        ImageView iv = (ImageView)v.findViewById(R.id.disk_package_icon);
        iv.setImageDrawable(info.icon);

        TextView tv = (TextView)v.findViewById(R.id.disk_package_summary);
        tv.setText(DiskData.getInstance().displaySize(info.mSize));

        TextView summary = (TextView)v.findViewById(R.id.disk_package_title);
        summary.setText(info.mAppName);

        //TODO: refactor to view holder
        CheckBox checkBox = (CheckBox)v.findViewById(R.id.disk_package_checkBox);
        checkBox.setId(position);

        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox)v;
                int id = cb.getId();
                if (mItemsChecked[id]){
                    cb.setChecked(false);
                    mItemsChecked[id] = false;
                } else {
                    cb.setChecked(true);
                    cb.setVisibility(View.VISIBLE);
                    mItemsChecked[id] = true;
                }
            }
        });
        return v;
    }

    public boolean[] getItemsChecked() {
        return mItemsChecked;
    }

    public String[] getItemsPath() {
        String[] itemsPath = new String[count];
        for (int i=0; i<count; i++) {
            itemsPath[i] = mPackages.get(i).mPath;
        }
        return itemsPath;
    }
}
