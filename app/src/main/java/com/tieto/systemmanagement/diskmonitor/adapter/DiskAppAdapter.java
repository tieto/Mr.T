package com.tieto.systemmanagement.diskmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.data.DiskData;
import com.tieto.systemmanagement.diskmonitor.entity.TApplicationInfo;
import com.tieto.systemmanagement.diskmonitor.model.BasicAdapter;
import com.tieto.systemmanagement.diskmonitor.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangbo on 4/3/15.
 */
public class DiskAppAdapter extends BasicAdapter {
    private Context mContext = null;
    private List<TApplicationInfo> mItems = null;
    private static LayoutInflater mInflater = null;
    private List<Integer> mItemsChecked;

    public DiskAppAdapter(Context context, List<TApplicationInfo> list) {
        mContext = context;
        mItems = list;
        mItemsChecked = new ArrayList<Integer>();

        for (int i=0; i<mItems.size();i++) {
            mItemsChecked.add(0);
        }

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        AppItemViewHolder holder;
        if (convertView == null) {
            holder = new AppItemViewHolder();
            convertView = mInflater.inflate(
                    R.layout.item_disk_app, null);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.disk_package_icon);
            holder.mCheckbox = (CheckBox) convertView.findViewById(R.id.disk_package_checkBox);
            holder.mTitle = (TextView) convertView.findViewById(R.id.disk_package_title);
            holder.mSizeDisplay = (TextView) convertView.findViewById(R.id.disk_package_summary);
            convertView.setTag(holder);

            holder.mCheckbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox)v;
                    int id = cb.getId();
                    if (Utils.int2Bool(mItemsChecked.get(id))){
                        cb.setChecked(false);

                        //TODO: how to solve the usage issue from List<String> and String[]
                        mItemsChecked.set(id,0);
                    } else {
                        cb.setChecked(true);
                        cb.setVisibility(View.VISIBLE);
                        mItemsChecked.set(id,1);
                    }
                }
            });
        }
        else {
            holder = (AppItemViewHolder) convertView.getTag();
        }

        TApplicationInfo info = mItems.get(position);
        holder.mImageView.setImageDrawable(info.icon);
        holder.mSizeDisplay.setText(DiskData.getInstance().displaySize(info.mSize));
        holder.mTitle.setText(info.mAppName);

        holder.mCheckbox.setChecked(Utils.int2Bool(mItemsChecked.get(position)));
        holder.mCheckbox.setId(position);

        return convertView;
    }

    public List<Integer> getItemsChecked() {
        return mItemsChecked;
    }

    public List<String> getItemsPath() {
        List<String> paths = new ArrayList<String>();
        for (int i=0; i< mItems.size();i++) {
            paths.add(mItems.get(i).mPath);
        }
        return paths;
    }

    public void deleteItem(String path) {
        String[] paths = new String[mItems.size()];
        for (int i=0; i< mItems.size();i++) {
            paths[i] = mItems.get(i).mPath;
            if (paths[i].equalsIgnoreCase(path)) {
                mItems.remove(i);
                mItemsChecked.remove(i);
            }
        }
    }
}
