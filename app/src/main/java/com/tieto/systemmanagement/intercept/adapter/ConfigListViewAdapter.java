package com.tieto.systemmanagement.intercept.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.intercept.util.InterceptConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaooked on 4/8/15.
 */
public class ConfigListViewAdapter extends BaseAdapter {

    private List<Map<String,Object>> data ;
    private LayoutInflater mInflater;
    private Context context ;
    private SharedPreferences sharedPreferences ;

    public ConfigListViewAdapter(Context context,List<Map<String,Object>> data){
        this.data =  data ;
        this.context = context ;
        sharedPreferences = context.getSharedPreferences(InterceptConfiguration.INTERCEPT_CONFIGURATION,context.MODE_PRIVATE) ;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.item_intercept_config_management, parent, false);
        } else {
            v = convertView;
        }

        bindView(position, v);

        return v;
    }

    private void bindView(final int position,View view){

        final Map<String, Object> date = data.get(position);
        TextView titleView = (TextView)view.findViewById(R.id.title) ;
        TextView titleEnableView = (TextView)view.findViewById(R.id.title_enable_intercept) ;
        Switch switchView = (Switch)view.findViewById(R.id.enable_intercept) ;

        titleView.setText(date.get("title_call_intercept").toString());
        titleEnableView.setText(date.get("title_is_intercept").toString());
        switchView.setSelected(Boolean.valueOf(date.get("intercept").toString()));

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(position).put("title_is_intercept",context.getResources().getString(isChecked ? R.string.intercept : R.string.un_intercept));
                data.get(position).put("intercept",isChecked);
                SharedPreferences.Editor edit = sharedPreferences.edit();
//                edit.putBoolean()
                notifyDataSetChanged();
            }
        });
    }
}
