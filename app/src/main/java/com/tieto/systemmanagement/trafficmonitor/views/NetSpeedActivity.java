package com.tieto.systemmanagement.trafficmonitor.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tieto.systemmanagement.BasicActivity;
import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.trafficmonitor.entity.TrafficSpeed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jane on 15-4-2.
 */
public class NetSpeedActivity extends BasicActivity implements View.OnClickListener{

    private TextView current_speed;
    private TextView average_speed;
    private Button calculate_speed;

    private static final String URL = "http://apk.hiapk.com/appdown/com.pianke.client?planid=692894" +
            "&seid=c68e2360-3720-0001-11d6-13201cb01f3f";
    private static final long POST_DELAY = 1000;
    private static final int UPDATE = 0;
    private static final int FINISHED =1;

    private boolean mFlag = true;
    private TrafficSpeed trafficSpeed;
    private Handler handler;
    private List<Long> speedList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_netspeed_display);
        current_speed = (TextView) findViewById(R.id.netspeed_current);
        average_speed = (TextView) findViewById(R.id.netspeed_average);
        calculate_speed = (Button) findViewById(R.id.netspeed_calculate);

        calculate_speed.setOnClickListener(this);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                long realtime_speed = trafficSpeed.getmSpeed()/1024;
                long average = 0;
                switch (msg.what){
                    case UPDATE://update the realtime speedInfo
                        current_speed.setText(realtime_speed+"");
                        speedList.add(realtime_speed);
                        Log.e("TAG","RT:"+realtime_speed);
                        break;
                    case FINISHED://update the realtime netspeed info and average netspeed info
                        current_speed.setText(realtime_speed+"");
                        speedList.add(realtime_speed);
                        int total = 0;
                        int totalCount = 0;
                        for (int i=0;i<speedList.size();i++) {
                            long temp = speedList.get(i);
                            if(temp != 0) {
                                total += speedList.get(i);
                                totalCount ++;
                            }

                        }
                        average = total/totalCount;
                        average_speed.setText(average+"");
                        calculate_speed.setClickable(true);
                        Log.e("TAG","AV:"+average);
                        break;
                }
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        //start to download file
        //每隔1000ms,更新一次当前的网速 temp,并保存当前的网速list
        //下载完成时,计算平均网速，并显示当前的网速
        current_speed.setText("   ");
        average_speed.setText("   ");
        Log.e("TAG","Begin to downLoad!!!");
        trafficSpeed = new TrafficSpeed();
        speedList = new ArrayList<Long>();
        calculate_speed.setClickable(false);
        new Thread(){
            @Override
            public void run() {
                super.run();
                //download file form assigned url,and update trafficSpeed info
                downLoadFile(URL, trafficSpeed);
            }

        }.start();

        new Thread() {
            @Override
            public void run() {
                super.run();
                while(trafficSpeed.getmHadReadBytes()<trafficSpeed.getmTotalBytes()|| trafficSpeed.getmHadReadBytes()==0){
                    if(mFlag) {
//                        if(trafficSpeed.getmHadReadBytes() != 0) {
                            handler.sendEmptyMessageDelayed(UPDATE,POST_DELAY);
//                        }

                    } else {
                        break;
                    }
                }
                handler.sendEmptyMessage(FINISHED);
            }
        }.start();
    }


    public void downLoadFile(String url_str, TrafficSpeed trafficSpeed) {
        URL url = null;
        URLConnection url_conn = null;
        InputStream input = null;
        long interval = 0;

        try {
            url  = new URL(url_str);
            url_conn = url.openConnection();
            url_conn.setConnectTimeout(2000);
            url_conn.setReadTimeout(2000);
            input = url_conn.getInputStream();

            int total = 0;
            total = url_conn.getContentLength();
            Log.e("TAG","total:"+total);
            trafficSpeed.setmTotalBytes(total);

            byte[] buff = new byte[1024];
            long startTime = System.currentTimeMillis();
            int len = 0;
                while((len = input.read()) != -1){
                    if(mFlag) {
                        trafficSpeed.setmHadReadBytes((trafficSpeed.getmHadReadBytes())+1);
                        interval = System.currentTimeMillis()-startTime;
                        if(interval == 0) {
                            trafficSpeed.setmSpeed(1000);
                        } else {
                            trafficSpeed.setmSpeed((trafficSpeed.getmHadReadBytes()/interval)*1000);
                        }
                    } else {
                        break;
                    }

                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //shasixiancheng
        mFlag = false;
        Log.e("TAG","destroy");

    }
}
