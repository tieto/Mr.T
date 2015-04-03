package com.tieto.systemmanagement.diskmonitor.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tieto.systemmanagement.R;

public class SystemSpaceFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_disk_system_space, container, false);
		
		return rootView;
	}
}
