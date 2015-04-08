package com.tieto.systemmanagement.trafficmonitor;

import android.content.Intent;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tieto.systemmanagement.BasicActivity;
import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.trafficmonitor.views.FireWallManageActivity;
import com.tieto.systemmanagement.trafficmonitor.views.NetSpeedActivity;


/**
 * Created by jane on 15-3-24.
 */
public class TrafficActivity extends BasicActivity implements View.OnClickListener{
   //main
    private TextView mCurrentNetSpededText;
    private Button mMeasureNetSpeedButton;
    private TextView mNetStateInfoText;
    private Button mNetManageButton;

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

    private String mNetType;
    private TApp mSysApplication;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_act_main);

        setupView();
        mSysApplication = (TApp) this.getApplication();
        mNetType  = mSysApplication.isNetConnected();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setContent();
    }

    //display the net info on the main_page
    private void setContent() {
        mNetStateInfoText.setText(mNetType);
        final TrafficStats trafficStats = new TrafficStats();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mCurrentNetSpededText.setText(getReadableString(msg.arg1));
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                while(true) {
                    long currentRxBytes = trafficStats.getTotalRxBytes();
                    try {
                        sleep(1000);
                        Message msg = Message.obtain();
                        long temp = trafficStats.getTotalRxBytes();
                        msg.arg1 = (int)(temp - currentRxBytes);
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

//        mCurrentNetSpededText.setText("");
    }

    /**
     *  initialize the layout
     */
    private void setupView() {

        mCurrentNetSpededText = (TextView)findViewById(R.id.act_main_large_number_view);
        mMeasureNetSpeedButton = (Button)findViewById(R.id.act_main_speed_btn);
        mNetStateInfoText = (TextView)findViewById(R.id.act_main_tip_tv);
        mNetManageButton = (Button)findViewById(R.id.act_main_app_msg_btn);

        current_traffic_left = (TextView)findViewById(R.id.act_main_plan_traffic_value_tv);
        traffic_unit = (TextView)findViewById(R.id.act_main_plan_traffic_unit_tv);
        traffic_type = (TextView)findViewById(R.id.act_main_plan_sim_label_tv);
        traffic_total = (TextView)findViewById(R.id.act_main_plan_total_value_tv);
        traffic_progress_bar = (ProgressBar)findViewById(R.id.act_main_plan_traffic_value_pb);

        mNetManageButton.setOnClickListener(this);
        mMeasureNetSpeedButton.setOnClickListener(this);
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
        if(TApp.TYPE_NONE.equals(mNetType)) {
            Toast.makeText(this,"无网络连接，请检查你的联网状态",Toast.LENGTH_SHORT).show();
        } else {
            int id = v.getId();
            switch (id){
                case R.id.act_main_app_msg_btn:
                    Intent intent = new Intent();
                    intent.setClass(TrafficActivity.this,FireWallManageActivity.class);
                    TrafficActivity.this.startActivity(intent);
                    break;
                case R.id.act_main_speed_btn:
                    Intent intent_speed = new Intent();
                    intent_speed.setClass(TrafficActivity.this, NetSpeedActivity.class);
                    TrafficActivity.this.startActivity(intent_speed);
                    break;
            }
        }
    }


}
