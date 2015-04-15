package com.tieto.systemmanagement;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.tieto.systemmanagement.startup.StartUpAdapter;

import java.util.ArrayList;
import java.util.List;


public class StartUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_start);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> data = new ArrayList<ResolveInfo>();
        List<ResolveInfo> apps = pm.queryBroadcastReceivers(new Intent(Intent.ACTION_BOOT_COMPLETED), PackageManager.GET_DISABLED_COMPONENTS);
        for(ResolveInfo info : apps) {
            if ((info.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                data.add(info);
            }
        }
        ListView listView = (ListView) findViewById(android.R.id.list);
        StartUpAdapter adapter = new StartUpAdapter(this, data);
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_self_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
