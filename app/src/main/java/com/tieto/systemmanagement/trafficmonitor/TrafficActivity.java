package com.tieto.systemmanagement.trafficmonitor;

import android.content.Intent;
import android.net.TrafficStats;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.trafficmonitor.views.FireWallManageActivity;

/**
 * Created by jane on 15-3-24.
 */
public class TrafficActivity extends BasicActivity implements View.OnClickListener{
   //main
    private TextView current_net_speed;
    private Button measure_net_speed;
    private TextView net_detail_info;
    private Button net_manage;
    //part_net
    private TextView current_traffic_left;
    private TextView traffic_unit;
    private TextView traffic_type;
    private TextView traffic_total;
    private ProgressBar traffic_progress_bar;
    //save
    private TextView save_level;
    private TextView save_value;
    private TextView save_value_unit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_act_main);
        //init all source id
        setupView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setContent();
//        String cmd = "chmod 777"+getPackageCodePath();
//                IptablesForDroidWall.RootCommand(cmd);


    }

    /**
     * display the net info on the main_page
     */
    private void setContent() {
        //if network is connected
        if(isNetConnected()) {
            net_detail_info.setText("网络连接正常");
            TrafficStats mTrafficStats = new TrafficStats();
            getCurrentNetInfo(mTrafficStats);
        }else {

        }





    }

    private void getCurrentNetInfo(TrafficStats trafficStats) {

    }

    /**
     *  initialize the layout
     */
    private void setupView() {

        current_net_speed = (TextView)findViewById(R.id.act_main_large_number_view);
        measure_net_speed = (Button)findViewById(R.id.act_main_speed_btn);
        net_detail_info = (TextView)findViewById(R.id.act_main_tip_tv);
        net_manage = (Button)findViewById(R.id.act_main_app_msg_btn);

        current_traffic_left = (TextView)findViewById(R.id.act_main_plan_traffic_value_tv);
        traffic_unit = (TextView)findViewById(R.id.act_main_plan_traffic_unit_tv);
        traffic_type = (TextView)findViewById(R.id.act_main_plan_sim_label_tv);
        traffic_total = (TextView)findViewById(R.id.act_main_plan_total_value_tv);
        traffic_progress_bar = (ProgressBar)findViewById(R.id.act_main_plan_traffic_value_pb);

        net_manage.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_traffic, menu);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.act_main_app_msg_btn:
                Intent intent = new Intent();
                intent.setClass(TrafficActivity.this,FireWallManageActivity.class);
                TrafficActivity.this.startActivity(intent);
                break;

        }
    }



}
