package com.tieto.systemmanagement.trafficmonitor.control;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.trafficmonitor.adapter.RealTimeTrafficSpeedAdapter;
import com.tieto.systemmanagement.trafficmonitor.entity.AppInfoEntity;
import com.tieto.systemmanagement.trafficmonitor.entity.TrafficStatsWrapper;
import com.tieto.systemmanagement.trafficmonitor.entity.TrafficSpeed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RealTimeNetworkSpeedFragment extends Fragment {

    private ListView mListView;
    private RealTimeTrafficSpeedAdapter mAdapter;
    private List<AppInfoEntity> mAppInfoList;
    private TrafficStatsWrapper mTrafficStats;

    public RealTimeNetworkSpeedFragment() {
        super();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.firewall_listView);
        mAppInfoList = new ArrayList<AppInfoEntity>();
        mTrafficStats = new TrafficStatsWrapper(this.getActivity());
        mAppInfoList = mTrafficStats.getmTrafficStaticAppInfoLists();
        mAdapter = new RealTimeTrafficSpeedAdapter(this.getActivity(), mAppInfoList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.t_firewall_fragement, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        TrafficSpeed.shutdownAll();
    }
}
