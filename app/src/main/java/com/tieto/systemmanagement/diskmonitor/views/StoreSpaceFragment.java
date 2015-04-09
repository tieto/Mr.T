package com.tieto.systemmanagement.diskmonitor.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.diskmonitor.DiskAlbumActivity;
import com.tieto.systemmanagement.diskmonitor.DiskAudioActivity;
import com.tieto.systemmanagement.diskmonitor.DiskPackagesActivity;
import com.tieto.systemmanagement.diskmonitor.adapter.DiskSpaceAdapter;
import com.tieto.systemmanagement.diskmonitor.data.DiskData;

public class StoreSpaceFragment extends Fragment {
    private ListView listView;
    private DiskSpaceAdapter adapter;

    public StoreSpaceFragment() {
        super();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView)view.findViewById(R.id.disk_list);
        adapter = new DiskSpaceAdapter((Context)(this.getActivity()), DiskData.getInstance().getStoreSpaceInfos());
        listView.setAdapter(adapter);
        TextView tv_summary_title = (TextView)view.findViewById(R.id.title);
        tv_summary_title.setText(TApp.getInstance().getString(R.string.disk_space_store));

        // Click event for single list row
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: {//packages
                        Intent intent = new Intent(getActivity(),DiskPackagesActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {//album
                        Intent intent = new Intent(getActivity(),DiskAlbumActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {//audio
                        Intent intent = new Intent(getActivity(),DiskAudioActivity.class);
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }

            }
        });


        //configured mem usage-> how to refactor
        TextView tv_summary_men_percent_used = (TextView)view.findViewById(R.id.used_percent);
        tv_summary_men_percent_used.setText(DiskData.getInstance().getMemPercentUsed()+"%");

        TextView tv_summary_men_free = (TextView)view.findViewById(R.id.used);
        tv_summary_men_free.setText(TApp.getInstance().getString(R.string.disk_space_used_title)
                +DiskData.getInstance().getStorageUsed()+"G/"
                +DiskData.getInstance().getStorageTotal()+"G");
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
