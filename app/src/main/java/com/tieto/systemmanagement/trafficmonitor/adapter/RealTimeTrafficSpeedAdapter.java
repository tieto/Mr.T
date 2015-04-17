package com.tieto.systemmanagement.trafficmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.authority.entity.AppWrapper;
import com.tieto.systemmanagement.trafficmonitor.entity.AppTrafficInfo;
import com.tieto.systemmanagement.trafficmonitor.entity.IptablesForDroidWall;
import com.tieto.systemmanagement.trafficmonitor.entity.TrafficSpeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jane on 15-3-26.
 */
public class RealTimeTrafficSpeedAdapter extends NetworkManageBasicAdapter {
    private List<AppTrafficInfo> mAppInfos;
    private LayoutInflater mInflater;

    public RealTimeTrafficSpeedAdapter(Context context, List<AppTrafficInfo> appInfos) {
        if(appInfos == null) {
            appInfos = new ArrayList<AppTrafficInfo>();
        }
        this.context = context;
        this.mAppInfos = appInfos;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.t_firewall_item_realtime,null);
            holder = new ViewHolder();
            holder.mAppIcon = (ImageView) convertView.findViewById(R.id.realtime_app_icon);
            holder.mAppName = (TextView) convertView.findViewById(R.id.realtime_app_name);
            holder.mAppNetSpeed = (TextView) convertView.findViewById(R.id.realtime_app_netspeed);
            holder.mNetAllowedInfo = (TextView) convertView.findViewById(R.id.realtime_net_allowed);
            holder.mAllowNetworkButton = (ImageButton) convertView.findViewById(R.id.realtime_allow_net);
            holder.mRealtime_firewall_panel = (LinearLayout) convertView.findViewById(
                    R.id.realtime_firewall_panel);
            holder.trafficSpeed = new TrafficSpeed();
            holder.speedListener = new SpeedListener(holder.mAppNetSpeed);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final AppTrafficInfo appInfo = mAppInfos.get(i);
        final AppWrapper appWrapper = appInfo.getmAppWrapper();
        holder.trafficSpeed.setUid(appWrapper.getUid());
        holder.trafficSpeed.registerUpdate(1000, holder.speedListener);

        holder.mAppIcon.setImageDrawable(appWrapper.loadIcon(context));
        holder.mAppName.setText(appWrapper.getName(context));
        holder.mNetAllowedInfo.setText(appInfo.getmFirewallType());

        final CallbackImpl impl = new CallbackImpl(holder.mNetAllowedInfo);
        final ViewHolder finalHolder = holder;
        holder.mRealtime_firewall_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IptablesForDroidWall.hasRootAccess(context, false)) {
                    showWindow(view, finalHolder.mAllowNetworkButton,appWrapper, impl);
                }
            }
        });
        return convertView;
    }

    private final static class ViewHolder {
        // Views
        private ImageView mAppIcon;
        private TextView mAppName;
        private TextView mAppNetSpeed;
        private TextView mNetAllowedInfo;
        private ImageButton mAllowNetworkButton;
        private LinearLayout mRealtime_firewall_panel;

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
