package com.tieto.systemmanagement.diskmonitor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.entity.AudioInfo;
import com.tieto.systemmanagement.diskmonitor.views.AudioItemViewHolder;

import java.util.List;

/**
 * Created by wangbo on 4/20/15.
 */
public class AudioItemAdapter  extends BaseAdapter {
    private Context mContext = null;
    private List<AudioInfo> mItems = null;
    private boolean [] mItemsChecked = null;

    private static LayoutInflater mInflater = null;
    final String LOG_TAG = this.toString();

    public AudioItemAdapter(Context context, List<AudioInfo> list) {
        mContext = context;
        mItems = list;
        mItemsChecked = new boolean[mItems.size()];

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

    public View getView(final int position, View convertView, ViewGroup parent) {
        final AudioInfo info = mItems.get(position);
        AudioItemViewHolder holder;
        if (convertView == null) {
            holder = new AudioItemViewHolder();
            convertView = mInflater.inflate(R.layout.item_disk_audio, null);

            holder.mTextView= (TextView) convertView.findViewById(R.id.title);
            holder.mTextView.setText(info.mFileName);

            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(holder);

            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e(LOG_TAG, "audio item " + " - "+ info);
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
        }
        else {
           holder = (AudioItemViewHolder) convertView.getTag();
        }

        holder.mCheckBox.setId(position);
        holder.mCheckBox.setChecked(mItemsChecked[position]);
        return convertView;
    }

    public boolean[] getItemsChecked( ) {
        return mItemsChecked;
    }

    public String[] getItemsPath() {
        String[] paths = new String[mItems.size()];
        for (int i=0; i< mItems.size();i++) {
            paths[i] = mItems.get(i).mPath;
        }
        return paths;
    }
}
