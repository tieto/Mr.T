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
public class ConfigCallListViewAdapter extends ConfigListViewAdapter {

    public ConfigCallListViewAdapter(Context context,List<Map<String,Object>> data){
        super(context,data);
    }

    @Override
    protected void bindView(final int position,View view){

        final Map<String, Object> date = data.get(position);
        TextView titleView = (TextView)view.findViewById(R.id.title) ;
        TextView titleEnableView = (TextView)view.findViewById(R.id.title_enable_intercept) ;
        Switch switchView = (Switch)view.findViewById(R.id.enable_intercept) ;

        titleView.setText(date.get("title_call_intercept").toString());
        titleEnableView.setText(date.get("title_is_intercept").toString());
        switchView.setChecked(Boolean.valueOf(date.get("intercept").toString()));

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(position).put("title_is_intercept",context.getResources().getString(isChecked ? R.string.intercept : R.string.un_intercept));
                data.get(position).put("intercept",isChecked);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                switch (position){
                    case 0 :
                        edit.putBoolean(InterceptHelper.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT,isChecked) ;
                        break;
                    case 1 :
                        edit.putBoolean(InterceptHelper.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_STRANGE,isChecked) ;
                        break;
                    case 2 :
                        edit.putBoolean(InterceptHelper.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_ANONYMITY,isChecked) ;
                        break;
                    case 3 :
                        edit.putBoolean(InterceptHelper.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_CONTRACT,isChecked) ;
                        break;
                }
                edit.commit() ;
                if(phoneFilterServer!=null){
                    phoneFilterServer.reInit();
                }
                notifyDataSetChanged();
            }
        });
    }
}
