package com.tieto.systemmanagement.diskmonitor.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tieto.systemmanagement.BasicActivity;
import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.adapter.AudioItemAdapter;
import com.tieto.systemmanagement.diskmonitor.data.DiskData;
import com.tieto.systemmanagement.diskmonitor.utils.FileUtils;
import com.tieto.systemmanagement.diskmonitor.utils.TFileManager;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DiskAudioActivity extends BasicActivity {
    private AudioItemAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disk_audio);
        final String LOG_TAG = this.toString();

        listView = (ListView)findViewById(R.id.listView);
        adapter = new AudioItemAdapter(this, DiskData.getInstance().getAudioData());
        listView.setAdapter(adapter);

        ImageButton btnCleanup = (ImageButton)findViewById(R.id.btnCleanup);
        btnCleanup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               final String[] pathSelected =  FileUtils.getItemSelected(adapter.getItemsChecked(),
                        adapter.getItemsPath());

                new SweetAlertDialog(DiskAudioActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("删除音乐")
                        .setContentText("删除后，音乐将不可找回，确认删除")
                        .setCancelText("取消")
                        .setConfirmText("任性一次")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                AsyncItemsRemoved itemRemoved = new AsyncItemsRemoved();
                                itemRemoved.execute(pathSelected);
                            }
                        })
                        .show();
            }
        });
    }

    private class AsyncItemsRemoved extends AsyncTask<String[], Void, ArrayList<String>> {
        private SweetAlertDialog pDialog;

        @Override
        protected void onPreExecute() {

            pDialog = new SweetAlertDialog(DiskAudioActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String[]... params) {
            int size = params[0].length;
            for(int i = 0; i < size; i++)
                TFileManager.getInstance().deleteTarget(params[0][i]);
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> file) {
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.notifyAll();

            final CharSequence[] names;
            int len = file != null ? file.size() : 0;
            pDialog.dismiss();
        }
    }
}
