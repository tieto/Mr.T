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

    private TextView mCurrentSpeedTextView;
    private TextView mAveragSpeedTextView;
    private Button mCalSpeedTextView;

    private static final String URL = "http://apk.hiapk.com/appdown/com.pianke.client?planid=692894" +
            "&seid=c68e2360-3720-0001-11d6-13201cb01f3f";
    private static final long POST_DELAY = 1000;
    private static final int UPDATE = 0;
    private static final int FINISHED =1;

    private boolean mFlag = true;
    private TrafficSpeed mTrafficSpeed;
    private Handler mHandler;
    private List<Long> mSpeedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_netspeed_display);
        mCurrentSpeedTextView = (TextView) findViewById(R.id.netspeed_current);
        mAveragSpeedTextView = (TextView) findViewById(R.id.netspeed_average);
        mCalSpeedTextView = (Button) findViewById(R.id.netspeed_calculate);
        mCalSpeedTextView.setOnClickListener(this);


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                long realtime_speed = mTrafficSpeed.getmSpeed();
                long average = 0;
                switch (msg.what){
                    case UPDATE://update the realtime speedInfo
                        mCurrentSpeedTextView.setText(getReadableString(realtime_speed));
                        if(realtime_speed!=0) {
                            mSpeedList.add(realtime_speed);
//                        Log.e("TAG","RT:"+realtime_speed);
                        }
                        break;
                    case FINISHED://update the realtime netspeed info and average netspeed info
                        mCurrentSpeedTextView.setText(getReadableString(realtime_speed));
                        if(realtime_speed != 0) {
                            mSpeedList.add(realtime_speed);
                        }
                        long total = 0;
                        int totalCount = 0;
                        for (int i=0;i< mSpeedList.size();i++) {
                            long temp = mSpeedList.get(i);
                            if(temp != 0) {
                                total =total + mSpeedList.get(i);
                                totalCount += 1;
                            }

                        }
                        Log.e("TAG","totalCount = "+totalCount+" ,total = "+total);
                        average = total/totalCount;
                        Log.e("TAG","average:"+average);
                        mAveragSpeedTextView.setText(getReadableString(average));
                        mCalSpeedTextView.setClickable(true);
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
        mCurrentSpeedTextView.setText("   ");
        mAveragSpeedTextView.setText("   ");
        Log.e("TAG","Begin to downLoad!!!");
        mTrafficSpeed = new TrafficSpeed();
        mSpeedList = new ArrayList<Long>();
        mCalSpeedTextView.setClickable(false);
        new Thread(){
            @Override
            public void run() {
                super.run();
                downLoadFile(URL, mTrafficSpeed);
            }

        }.start();

        new Thread() {
            @Override
            public void run() {
                super.run();
                while(mTrafficSpeed.getmHadReadBytes()< mTrafficSpeed.getmTotalBytes()|| mTrafficSpeed.getmHadReadBytes()==0){
                    if(mFlag) {
                            mHandler.sendEmptyMessageDelayed(UPDATE, POST_DELAY);
                    } else {
                        break;
                    }
                }
                mHandler.sendEmptyMessage(FINISHED);
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
        mFlag = false;
    }



}
