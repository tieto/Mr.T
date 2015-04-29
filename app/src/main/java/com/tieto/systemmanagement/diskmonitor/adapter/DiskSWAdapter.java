package com.tieto.systemmanagement.diskmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.data.DiskData;
import com.tieto.systemmanagement.diskmonitor.entity.TSWInfo;
import com.tieto.systemmanagement.diskmonitor.model.BasicAdapter;
import com.tieto.systemmanagement.diskmonitor.utils.DebugToast;

import java.util.List;

/**
 * Created by wangbo on 4/3/15.
 */
public class DiskSWAdapter extends BasicAdapter {
    private Context mContext = null;
    private List<TSWInfo> mItems = null;
    private static LayoutInflater mInflater = null;

    public DiskSWAdapter(Context context, List<TSWInfo> list) {
        mContext = context;
        mItems = list;

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

        final SWItemViewHolder holder;
        if (convertView == null) {
            holder = new SWItemViewHolder();
            convertView = mInflater.inflate(
                    R.layout.item_disk_sw, null);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.disk_sw_icon);
            holder.mTitle = (TextView) convertView.findViewById(R.id.disk_sw_title);
            holder.mSizeDisplay = (TextView) convertView.findViewById(R.id.disk_sw_size);
            holder.mBtnUninstall = (Button) convertView.findViewById(R.id.disk_sw_remove);
            convertView.setTag(holder);

            holder.mBtnUninstall.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Button btn = (Button)v;
                    final int p = btn.getId();
                    updateUI(p);
                    DebugToast.debugToast("",""+holder.mTitle.getText());
                }
            });
        }
        else {
            holder = (SWItemViewHolder) convertView.getTag();
        }

        TSWInfo info = mItems.get(position);
        holder.mImageView.setImageDrawable(info.icon);
        holder.mSizeDisplay.setText(DiskData.getInstance().displaySize(info.mSize));
        holder.mTitle.setText(info.mAppName);
        holder.mBtnUninstall.setId(position);

        return convertView;
    }

    private void updateUI(int p) {
        mItems.remove(p);
        notifyDataSetChanged();
    }
}
