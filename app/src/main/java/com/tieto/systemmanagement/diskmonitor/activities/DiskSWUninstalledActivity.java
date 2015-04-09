package com.tieto.systemmanagement.diskmonitor.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tieto.systemmanagement.BasicTabbedActivity;
import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.diskmonitor.views.UninstalledSysSWFragment;
import com.tieto.systemmanagement.diskmonitor.views.UninstalledUserSWFragment;

import java.util.ArrayList;
import java.util.List;

public class DiskSWUninstalledActivity extends BasicTabbedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disk_uninstalled);
        this.getActionBar().hide();
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Class<? extends Fragment>> fs = new ArrayList<Class<? extends Fragment>>();
        fs.add(UninstalledUserSWFragment.class);
        fs.add(UninstalledSysSWFragment.class);

        List<String> ts = new ArrayList<String>();
        ts.add(TApp.getInstance().getString(R.string.disk_uninstalled_user_sw));
        ts.add(TApp.getInstance().getString(R.string.disk_uninstalled_sys_sw));

        try {
            set2Tabs(fs, ts);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
