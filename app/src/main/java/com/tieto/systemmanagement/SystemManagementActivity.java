package com.tieto.systemmanagement;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tieto.systemmanagement.trafficmonitor.TrafficActivity;

import java.util.ArrayList;
import java.util.List;

public class SystemManagementActivity extends Activity {

    private List<FunEntity> acitivities = new ArrayList<FunEntity>();
    private Resources res;
    private SystemManagementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_management);
        res = getResources();
        GridView contentView = (GridView) findViewById(R.id.container);
        addData();
        adapter = new SystemManagementAdapter(this, acitivities);
        contentView.setAdapter(adapter);
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SystemManagementActivity.this, ((FunEntity) adapter.getItem(position)).getActivityclass());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_system_management, menu);
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

    private void addData() {
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_process_management), res.getDrawable(R.mipmap.ic_launcher, null), ProcessActivity.class));
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_memory), res.getDrawable(R.mipmap.ic_launcher, null), MemoryActivity.class));
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_notification), res.getDrawable(R.mipmap.ic_launcher, null), NotificationActivity.class));
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_app), res.getDrawable(R.mipmap.ic_launcher, null), AppActivity.class));
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_authority), res.getDrawable(R.mipmap.ic_launcher, null), AuthorityActivity.class));
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_self_start), res.getDrawable(R.mipmap.ic_launcher, null), SelfStartActivity.class));
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_traffic), res.getDrawable(R.mipmap.ic_launcher, null), TrafficActivity.class));
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_disk), res.getDrawable(R.mipmap.ic_launcher, null), DiskActivity.class));
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_no_spam), res.getDrawable(R.mipmap.ic_launcher, null), NoSpamActivity.class));
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_net_control), res.getDrawable(R.mipmap.ic_launcher, null), NetControlActivity.class));
        acitivities.add(new FunEntity(res.getString(R.string.title_activity_battery), res.getDrawable(R.mipmap.ic_launcher, null), BatteryActivity.class));
    }
}
