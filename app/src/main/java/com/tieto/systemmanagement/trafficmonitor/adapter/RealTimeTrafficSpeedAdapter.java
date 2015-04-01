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
import com.tieto.systemmanagement.trafficmonitor.entity.TrafficSpeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jane on 15-3-26.
 */
public class RealTimeTrafficSpeedAdapter extends BasicAdapter {
    private List<AppInfoEntity> appInfos;
    private LayoutInflater inflater;

    public RealTimeTrafficSpeedAdapter(Context context, List<AppInfoEntity> appInfos) {
        if(appInfos == null) {
            appInfos = new ArrayList<AppInfoEntity>();
        }
        this.context = context;
        this.appInfos = appInfos;
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
        if(view == null) {
            view = inflater.inflate(R.layout.t_firewall_item_realtime,null);
            holder = new ViewHolder();
            holder.appIcon = (ImageView) view.findViewById(R.id.realtime_app_icon);
            holder.appName = (TextView) view.findViewById(R.id.realtime_app_name);
            holder.appNetSpeed = (TextView) view.findViewById(R.id.realtime_app_netspeed);
            holder.net_allowed_info = (TextView) view.findViewById(R.id.realtime_net_allowed);
            holder.allowNetwork = (LinearLayout) view.findViewById(R.id.realtime_allow_net);

            holder.trafficSpeed = new TrafficSpeed();
            holder.speedListener = new SpeedListener(holder.appNetSpeed);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        AppInfoEntity appInfo = appInfos.get(i);

        holder.trafficSpeed.setUid(appInfo.getUid());
        holder.trafficSpeed.registerUpdate(1000, holder.speedListener);

        holder.appIcon.setImageDrawable(appInfo.getAppIcon());
        holder.appName.setText(appInfo.getAppName());
        holder.net_allowed_info.setText(appInfo.getIsNetworkAllowed());
        holder.allowNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWindow(view);
            }
        });
        return view;
    }

    private final static class ViewHolder {
        // Views
        private ImageView appIcon;
        private TextView appName;
        private TextView appNetSpeed;
        private TextView net_allowed_info;
        private LinearLayout allowNetwork;

        // Other objects
        private TrafficSpeed trafficSpeed;
        private SpeedListener speedListener;
    }

    private final static class SpeedListener implements TrafficSpeed.OnSpeedUpdatedListener {

        private TextView mTarget = null;

        public SpeedListener(TextView target) {
            mTarget = target;
        }

        @Override
        public void onSpeedUpdated(TrafficSpeed.Speeds speeds) {
            mTarget.setText(speeds.getRxSpeedReadable());
        }
    }
}
