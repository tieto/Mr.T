package com.tieto.systemmanagement.diskmonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tieto.systemmanagement.BasicTabbedActivity;
import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.diskmonitor.views.StoreSpaceFragment;
import com.tieto.systemmanagement.diskmonitor.views.SystemSpaceFragment;

import java.util.ArrayList;
import java.util.List;

public class DiskActivity  extends BasicTabbedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disk);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Class<? extends Fragment>> fs = new ArrayList<Class<? extends  Fragment>>();
        fs.add(StoreSpaceFragment.class);
        fs.add(SystemSpaceFragment.class);

        List<String> ts = new ArrayList<String>();
        ts.add(TApp.getInstance().getString(R.string.disk_space_store));
        ts.add(TApp.getInstance().getString(R.string.disk_space_system));

        try {
            set2Tabs(fs, ts);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
