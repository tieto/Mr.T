package com.tieto.systemmanagement.diskmonitor.model;

import android.content.Context;

import com.tieto.systemmanagement.diskmonitor.utils.CmdUtils;

import java.util.ArrayList;

/**
 * Created by public on 4/23/15.
 */

public class AsyncItemRemovedPermission extends AsyncItemRemoved {

    public  AsyncItemRemovedPermission(Context context,BasicAdapter adapter) {
        setAdapter(adapter);
        setContext(context);
    }

    @Override
    protected ArrayList<String> doInBackground(String[]... params) {
        int size = params[0].length;
        for(int i = 0; i < size; i++){
            getAdapter().deleteItem(params[0][i]);
            CmdUtils.executeCmd(params[0][i]);
        }
        return null;
    }
}