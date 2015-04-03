package com.tieto.systemmanagement.diskmonitor.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.adapter.DiskSpaceAdapter;
import com.tieto.systemmanagement.diskmonitor.entity.SpaceInfo;

import java.util.ArrayList;
import java.util.List;

public class StoreSpaceFragment extends Fragment {
    private ListView listView;
    private List<SpaceInfo> spaceInfos;
    private DiskSpaceAdapter adapter;

    public StoreSpaceFragment() {
        super();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView)view.findViewById(R.id.disk_list);
        spaceInfos = new ArrayList<SpaceInfo>();
        adapter = new DiskSpaceAdapter((Context)(this.getActivity()),spaceInfos);
        listView.setAdapter(adapter);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_disk_store_space, container, false);
		return rootView;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
