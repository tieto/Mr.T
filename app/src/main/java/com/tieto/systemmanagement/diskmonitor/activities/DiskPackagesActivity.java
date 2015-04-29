package com.tieto.systemmanagement.diskmonitor.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tieto.systemmanagement.BasicTabbedActivity;
import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.diskmonitor.views.DiskAppsInstalledFragment;
import com.tieto.systemmanagement.diskmonitor.views.DiskAppsUninstalledFragment;

import java.util.ArrayList;
import java.util.List;

public class DiskPackagesActivity  extends BasicTabbedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disk_packages);
        this.getActionBar().hide();
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Class<? extends Fragment>> fs = new ArrayList<Class<? extends Fragment>>();
        fs.add(DiskAppsInstalledFragment.class);
        fs.add(DiskAppsUninstalledFragment.class);

        List<String> ts = new ArrayList<String>();
        ts.add(TApp.getInstance().getString(R.string.disk_space_store_package_title_1));
        ts.add(TApp.getInstance().getString(R.string.disk_space_store_package_title_2));

        try {
            set2Tabs(fs, ts);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
