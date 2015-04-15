package com.tieto.systemmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tieto.systemmanagement.authority.AuthorityActivity;
import com.tieto.systemmanagement.diskmonitor.activities.DiskActivity;
import com.tieto.systemmanagement.intercept.views.InterceptActivity;
import com.tieto.systemmanagement.processmanage.ProcessActivity;
import com.tieto.systemmanagement.trafficmonitor.TrafficActivity;

import java.util.ArrayList;
import java.util.List;

public class SystemManagementActivity extends Activity {

    private final static List<FunEntity> ACTIVITIES = new ArrayList<FunEntity>();
    //TODO:the class member var need start m
    private SystemManagementAdapter adapter;

    static {
        ACTIVITIES.add(new FunEntity(R.string.title_activity_process_management
                , R.mipmap.dh, ProcessActivity.class));
//        ACTIVITIES.add(new FunEntity(R.string.title_activity_memory
//                , R.mipmap.card_icon_speedup, MemoryActivity.class));
        ACTIVITIES.add(new FunEntity(R.string.title_activity_notification
                , R.mipmap.hl, NotificationActivity.class));
        ACTIVITIES.add(new FunEntity(R.string.title_activity_app
                , R.mipmap.ih, AppActivity.class));
        ACTIVITIES.add(new FunEntity(R.string.title_activity_authority
                , R.mipmap.ql, AuthorityActivity.class));
        ACTIVITIES.add(new FunEntity(R.string.title_activity_self_start
                , R.mipmap.card_icon_autorun, StartUpActivity.class));
        ACTIVITIES.add(new FunEntity(R.string.title_activity_traffic
                , R.mipmap.t_app_icon, TrafficActivity.class));
        ACTIVITIES.add(new FunEntity(R.string.title_activity_disk
                , R.mipmap.card_icon_media, DiskActivity.class));
        ACTIVITIES.add(new FunEntity(R.string.title_activity_no_spam
                , R.mipmap.jm, InterceptActivity.class));
//        ACTIVITIES.add(new FunEntity(R.string.title_activity_net_control
//                , R.mipmap.wl, NetControlActivity.class));
        ACTIVITIES.add(new FunEntity(R.string.title_activity_battery
                , R.mipmap.sl, BatteryActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_management);
        GridView contentView = (GridView) findViewById(R.id.container);
        adapter = new SystemManagementAdapter(this, ACTIVITIES);
        contentView.setAdapter(adapter);
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SystemManagementActivity.this,
                        adapter.getItem(position).getActivity());
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_system_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
