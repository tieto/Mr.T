package com.tieto.systemmanagement.trafficmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.trafficmonitor.entity.AppInfoEntity;
import com.tieto.systemmanagement.trafficmonitor.entity.IptablesForDroidWall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jane on 15-3-26.
 */
public class MonthTrafficStaticAdapter extends BasicAdapter {
    private List<AppInfoEntity> mAppInfos;
    private LayoutInflater mInflater;

    public MonthTrafficStaticAdapter(Context context, List<AppInfoEntity> appInfos) {
        if(appInfos == null) {
            appInfos = new ArrayList<AppInfoEntity>();
        }
        this.mAppInfos = appInfos;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mAppInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return mAppInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view==null) {
            view = mInflater.inflate(R.layout.t_firewall_item_month_traffic_info,null);
            holder = new ViewHolder();
            holder.mAppIcon = (ImageView) view.findViewById(R.id.mon_app_icon);
            holder.mAppName = (TextView) view.findViewById(R.id.mon_app_name);
            holder.mTrffic_sneak_tip = (TextView) view.findViewById(R.id.mon_traffic_sneak_tip);
            holder.mTraffic_used = (TextView) view.findViewById(R.id.mon_traffic_used);
            holder.mTraffic_used_bg = (TextView) view.findViewById(R.id.mon_traffic_bg);
            holder.mTraffic_sneaked = (TextView) view.findViewById(R.id.mon_traffic_sneak);
            holder.mNet_allowed_info = (TextView) view.findViewById(R.id.mon_net_allowed_info);
            holder.mAllowNetwork = (ImageButton) view.findViewById(R.id.mon_allow_net);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        final AppInfoEntity appInfo = mAppInfos.get(i);
        holder.mAppIcon.setImageDrawable(appInfo.getmAppIcon());
        holder.mAppName.setText(appInfo.getmAppName());
        holder.mTraffic_used.setText("已用" + appInfo.getmAppTrafficUsed() + "M");
        holder.mTraffic_used_bg.setText("后台" + appInfo.getmAppTrafficUsedBg() + "M");
        holder.mTraffic_sneaked.setText("本月已偷跑" + appInfo.getmAppTrafficSneaked() + "M");
        holder.mNet_allowed_info.setText(appInfo.getmIsNetworkAllowed());

        final CallbackImpl impl = new CallbackImpl(holder.mNet_allowed_info);
        holder.mAllowNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //popup window,and when  the item is clicked ,the info should be changed.
                if (IptablesForDroidWall.hasRootAccess(context, true)) {
                    showWindow(view, appInfo, impl);
                }
                showWindow(view, appInfo, impl);

            }
        });
        return view;
    }

    private class ViewHolder {
        private ImageView mAppIcon;
        private TextView mAppName;
        private TextView mTrffic_sneak_tip;
        private TextView mTraffic_used;
        private TextView mTraffic_used_bg;
        private TextView mTraffic_sneaked;
        private TextView mNet_allowed_info;
        private ImageButton mAllowNetwork;
    }
}
