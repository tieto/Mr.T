package com.tieto.systemmanagement.trafficmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.trafficmonitor.entity.AppInfoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jane on 15-3-26.
 */
public class MonthTrafficStaticAdapter extends BasicAdapter {
    private List<AppInfoEntity> appInfos;
    private Context context;
    private LayoutInflater inflater;

    public MonthTrafficStaticAdapter(Context context, List<AppInfoEntity> appInfos) {
        if(appInfos == null) {
            appInfos = new ArrayList<AppInfoEntity>();
        }
        this.appInfos = appInfos;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return appInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view==null) {
            view = inflater.inflate(R.layout.t_firewall_item_month_traffic_info,null);
            holder = new ViewHolder();
            holder.appIcon = (ImageView) view.findViewById(R.id.mon_app_icon);
            holder.appName = (TextView) view.findViewById(R.id.mon_app_name);
            holder.trffic_sneak_tip = (TextView) view.findViewById(R.id.mon_traffic_sneak_tip);
            holder.traffic_used = (TextView) view.findViewById(R.id.mon_traffic_used);
            holder.traffic_used_bg = (TextView) view.findViewById(R.id.mon_traffic_bg);
            holder.traffic_sneaked = (TextView) view.findViewById(R.id.mon_traffic_sneak);
            holder.net_allowed_info = (TextView) view.findViewById(R.id.mon_net_allowed_info);
            holder.allowNetwork = (LinearLayout) view.findViewById(R.id.mon_allow_net);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        AppInfoEntity appInfo = appInfos.get(i);
        holder.appIcon.setImageDrawable(appInfo.getAppIcon());
        holder.appName.setText(appInfo.getAppName());
        holder.traffic_used.setText("已用"+appInfo.getAppTrafficUsed()+"M");
        holder.traffic_used_bg.setText("后台"+appInfo.getAppTrafficUsedBg()+"M");
        holder.traffic_sneaked.setText("本月已偷跑"+appInfo.getAppTrafficSneaked()+"M");
        holder.net_allowed_info.setText(appInfo.getIsNetworkAllowed());
        holder.allowNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //popup window,and when  the item is clicked ,the info should be changed.
            }
        });
        return view;
    }

    private class ViewHolder {
        private ImageView appIcon;
        private TextView appName;
        private TextView trffic_sneak_tip;
        private TextView traffic_used;
        private TextView traffic_used_bg;
        private TextView traffic_sneaked;
        private TextView net_allowed_info;
        private LinearLayout allowNetwork;
    }
}
