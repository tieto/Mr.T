package com.tieto.systemmanagement.trafficmonitor.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;


import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.trafficmonitor.adapter.ViewPagerAdapter;
import com.tieto.systemmanagement.trafficmonitor.entity.IptablesForDroidWall;

import java.util.ArrayList;

/**
 * Created by jane on 15-3-25.
 */
public class NetworkManageActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{
    private ArrayList<Fragment> mFragments;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private TextView mRTNetSpeedTextView;
    private TextView mMonthlyTrafficStastic;
    private View mCursor;

    private int mCurrentIndex = 0;
    private long mOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity_firewall_manage_layout);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initialize() {
        mCursor = findViewById(R.id.activity_firewall_cursor);
        mViewPager = (ViewPager) findViewById(R.id.activity_firewall_viewpager);
        mRTNetSpeedTextView = (TextView) findViewById(R.id.activity_firewall_realtime);
        mMonthlyTrafficStastic = (TextView) findViewById(R.id.activity_firewall_monthly);
        mRTNetSpeedTextView.setOnClickListener(new MyOnClickListener(0));
        mMonthlyTrafficStastic.setOnClickListener(new MyOnClickListener(1));

        mOffset = mCursor.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        mOffset = (int) (screenW / 2.0 - mOffset);

        mFragments = new ArrayList<Fragment>();
        mFragments.add(new RealTimeNetworkSpeedFragment());
        mFragments.add(new MonthTrafficStasticFragement());
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //check if hasRoot permission ,and apply the iptable firewall rules
        if(IptablesForDroidWall.hasRootAccess(this,false)) {
            IptablesForDroidWall.applySavedIptablesRules(NetworkManageActivity.this,true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Animation anim = null;
        switch (position) {
            case 0:
                if(mCurrentIndex ==1) {
                    anim =  new TranslateAnimation(mOffset,0,0,0);
                }
                break;
            case 1:
                if(mCurrentIndex ==0) {
                    anim = new TranslateAnimation(0, mOffset,0,0);
                }
                break;
        }
        mCurrentIndex = position;
        anim.setDuration(500);
        anim.setFillAfter(true);
        anim.setRepeatCount(0);
        anim.setRepeatMode(Animation.REVERSE);
        mCursor.startAnimation(anim);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;
        private MyOnClickListener(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View view) {
            mViewPager.setCurrentItem(index);
        }
    }
}
