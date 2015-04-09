package com.tieto.systemmanagement.diskmonitor.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.adapter.DiskStorageAdapter;

public class UninstalledSysSWFragment extends Fragment {
    private ListView listView;
    private DiskStorageAdapter adapter;

    public UninstalledSysSWFragment() {
        super();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        listView = (ListView) view.findViewById(R.id.disk_list);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_uninstalled_sys_sw, container, false);
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
