package com.tieto.systemmanagement.diskmonitor.model;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.tieto.systemmanagement.diskmonitor.utils.TFileManager;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by public on 4/23/15.
 */

public class AsyncItemRemoved extends AsyncTask<String[], Void, ArrayList<String>> {
    private SweetAlertDialog pDialog;
    private Context mContext;
    private BasicAdapter mAdapter;

    public  AsyncItemRemoved(Context context,BasicAdapter adapter) {
        mContext = context;
        mAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {

        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected ArrayList<String> doInBackground(String[]... params) {
        int size = params[0].length;
        for(int i = 0; i < size; i++){
            //TODO: good way to update UI by update data source (list)
            mAdapter.deleteItem(params[0][i]);
            TFileManager.getInstance().deleteTarget(params[0][i]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(final ArrayList<String> file) {
        mAdapter.notifyDataSetChanged();

        final CharSequence[] names;
        int len = file != null ? file.size() : 0;
        pDialog.dismiss();
    }
}