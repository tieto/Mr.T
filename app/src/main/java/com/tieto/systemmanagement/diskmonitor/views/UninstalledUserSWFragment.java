package com.tieto.systemmanagement.diskmonitor.views;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.adapter.DiskSWAdapter;
import com.tieto.systemmanagement.diskmonitor.data.DiskData;
import com.tieto.systemmanagement.diskmonitor.entity.TSWInfo;

import java.net.URL;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UninstalledUserSWFragment extends Fragment {
    private ListView listView;
    private DiskSWAdapter adapter;
    private Context mContext;
    private SweetAlertDialog mDialog;

    public UninstalledUserSWFragment() {
        super();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_uninstalled_user_sw, container, false);
		return rootView;
	}

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView)getView().findViewById(R.id.listView);
        mContext = this.getActivity();

        mDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("卖力获取中");
        mDialog.setCancelable(false);
        mDialog.show();

        ASyncSW syncPackages = new ASyncSW();
        syncPackages.execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class ASyncSW extends AsyncTask<URL, Integer, List<TSWInfo>> {
        protected List<TSWInfo> doInBackground(URL... arg0) {
            return DiskData.getInstance().getUserInstalledSW();
        }

        protected void onPostExecute(List<TSWInfo> result) {
            try {
                adapter = new DiskSWAdapter(mContext,result);
                listView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mDialog.dismiss();
        }
    }
}
