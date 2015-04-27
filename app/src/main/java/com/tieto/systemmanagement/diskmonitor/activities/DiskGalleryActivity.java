
package com.tieto.systemmanagement.diskmonitor.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.diskmonitor.adapter.GalleryItemAdapter;
import com.tieto.systemmanagement.diskmonitor.data.DiskData;
import com.tieto.systemmanagement.diskmonitor.entity.ThumbnailInfo;
import com.tieto.systemmanagement.diskmonitor.model.AsyncItemRemoved;
import com.tieto.systemmanagement.diskmonitor.utils.FileUtils;

import java.net.URL;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DiskGalleryActivity extends Activity implements OnItemClickListener {

    private GalleryItemAdapter imageAdapter;
    private SweetAlertDialog mDialog;
    private GridView mImageGrid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disk_gallery);
        mImageGrid = (GridView) findViewById(R.id.galleryGrid);
        mImageGrid.setOnItemClickListener(this);

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (isSDPresent) {
            mDialog = new SweetAlertDialog(DiskGalleryActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            mDialog.setTitleText("卖力获取中");
            mDialog.setCancelable(false);
            mDialog.show();

            ASyncImages syncImagesTask = new ASyncImages();
            syncImagesTask.execute();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry,No SD Card Found On This Device",
                    Toast.LENGTH_LONG).show();
        }


        final ImageButton btnCleanup = (ImageButton) findViewById(R.id.btnCleanup);
        btnCleanup.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final String[] pathSelected = FileUtils.getItemSelected(imageAdapter.getItemsChecked(),
                        imageAdapter.getItemsPath());

                try {
                    if (pathSelected.length > 0) {
                        new SweetAlertDialog(DiskGalleryActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("删除图片")
                                .setContentText("删除后，图片将不可找回，确认删除")
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
                                        AsyncItemRemoved itemRemoved = new AsyncItemRemoved(DiskGalleryActivity.this, imageAdapter);
                                        itemRemoved.execute(pathSelected);
                                    }
                                })
                                .show();
                    }
                }
                catch (Exception e) {

                }
            }
        });
    }

    class ASyncImages extends AsyncTask<URL, Integer, List<ThumbnailInfo>> {
        protected List<ThumbnailInfo> doInBackground(URL... arg0) {
            return DiskData.getInstance().getThumbnailData();
        }

        protected void onPostExecute(List<ThumbnailInfo> result) {
            try {
                imageAdapter = new GalleryItemAdapter(result);
                mImageGrid.setAdapter(imageAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mDialog.dismiss();
                }
            }, 2);
        }
    }

    public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
        Toast.makeText(getApplicationContext(),
                "to extend...",
                Toast.LENGTH_LONG).show();
    }
}