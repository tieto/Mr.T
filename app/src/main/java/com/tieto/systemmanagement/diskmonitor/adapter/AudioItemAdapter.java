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
import com.tieto.systemmanagement.diskmonitor.views.AudioItemViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbo on 4/20/15.
 */
public class AudioItemAdapter  extends BaseAdapter {
    private Context mContext = null;
    private List<String> mArray = null;
    private Map<String, Integer> mCheckedMap = null;

    private static LayoutInflater mInflater = null;
    final String LOG_TAG = this.toString();

    public AudioItemAdapter(Context context, List<String> list) {
        mContext = context;
        mArray = list;
        mCheckedMap = new HashMap<String, Integer>();

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mArray.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final CheckBox checkBox;
        final TextView textView;
        final String info = mArray.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_disk_audio, null);
            textView = (TextView) convertView.findViewById(R.id.title);
            textView.setText(info);

            checkBox = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(new AudioItemViewHolder(textView, checkBox));
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e(LOG_TAG, "audio item " + " - "+ info);
                    mCheckedMap.put(info,isChecked(checkBox.isChecked()));
                }
            });
        }
        else {

            /**
             * Reuse:Using a ViewHolder to avoid having to call findViewById().
             */
            AudioItemViewHolder viewHolder = (AudioItemViewHolder) convertView.getTag();
            textView = viewHolder.getTextView();
            textView.setText(info);

        }
        return convertView;
    }

    public Map<String, Integer> getCheckedItems( ) {
        return mCheckedMap;
    }

        private Integer isChecked(boolean b) {
        return b?1:0;
    }
}
