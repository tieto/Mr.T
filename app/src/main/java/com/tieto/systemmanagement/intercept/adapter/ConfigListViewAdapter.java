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
import com.tieto.systemmanagement.intercept.service.PhoneFilterServer;
import com.tieto.systemmanagement.intercept.util.InterceptHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaooked on 4/8/15.
 */
public abstract class ConfigListViewAdapter extends BaseAdapter {

    protected List<Map<String,Object>> data ;
    protected LayoutInflater mInflater;
    protected Context context ;
    protected SharedPreferences sharedPreferences ;
    protected PhoneFilterServer phoneFilterServer;

    public ConfigListViewAdapter(){

    }

    public ConfigListViewAdapter(Context context,List<Map<String,Object>> data){
        this.data =  data ;
        this.context = context ;
        sharedPreferences = context.getSharedPreferences(InterceptHelper.INTERCEPT_CONFIGURATION,context.MODE_PRIVATE) ;
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

    abstract void bindView(final int position,View view) ;

    public void setPhoneFilterServer(PhoneFilterServer phoneFilterServer){
        this.phoneFilterServer = phoneFilterServer ;
    }
}
