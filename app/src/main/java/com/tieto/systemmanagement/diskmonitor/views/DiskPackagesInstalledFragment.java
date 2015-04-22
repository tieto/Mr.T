package com.tieto.systemmanagement.diskmonitor.views;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.adapter.DiskPackageAdapter;
import com.tieto.systemmanagement.diskmonitor.data.DiskData;
import com.tieto.systemmanagement.diskmonitor.entity.ProcessInfo;
import com.tieto.systemmanagement.diskmonitor.utils.FileUtils;

import java.net.URL;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DiskPackagesInstalledFragment extends Fragment {
    private ListView listView;
    private DiskPackageAdapter adapter;
    private Context mContext;
    private SweetAlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_disk_packages_installed, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView)getView().findViewById(R.id.listView);
        mContext = this.getActivity();

        mDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("卖力获取中");
        mDialog.setCancelable(false);
        mDialog.show();

        final ImageButton btnCleanup = (ImageButton) getView().findViewById(R.id.btnCleanup);
        btnCleanup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FileUtils.getItemSelected(adapter.getItemsChecked(), adapter.getItemsPath());
            }
        });

        SyncPackages syncPackages = new SyncPackages();
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

    class SyncPackages extends AsyncTask<URL, Integer, List<ProcessInfo>> {
        protected List<ProcessInfo> doInBackground(URL... arg0) {
            return DiskData.getInstance().getPackages();
        }

        protected void onPostExecute(List<ProcessInfo> result) {
            try {
                adapter = new DiskPackageAdapter(mContext,result);
                listView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mDialog.dismiss();
        }
    }
}


