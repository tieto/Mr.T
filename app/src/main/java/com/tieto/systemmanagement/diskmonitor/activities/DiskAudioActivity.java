package com.tieto.systemmanagement.diskmonitor.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tieto.systemmanagement.BasicActivity;
import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.adapter.AudioItemAdapter;
import com.tieto.systemmanagement.diskmonitor.data.DiskData;

import java.util.Map;

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
              Map<String,Integer> itemChecked = adapter.getCheckedItems();
                Log.e(LOG_TAG, "clean " + itemChecked);

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

                                //TODO
                            }
                        })
                        .show();
            }
        });
    }
}
