package com.tieto.systemmanagement.diskmonitor;

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
import com.tieto.systemmanagement.diskmonitor.views.StoreSpaceFragment;
import com.tieto.systemmanagement.diskmonitor.views.SystemSpaceFragment;
import com.tieto.systemmanagement.trafficmonitor.adapter.ViewPagerAdapter;

import java.util.ArrayList;

//Refer to FireWallManageActivity
public class DiskActivity  extends FragmentActivity implements ViewPager.OnPageChangeListener{
    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TextView tv_store;
    private TextView tv_system;
    private View cursor;

    private int currentIndex = 0;
    private long offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disk);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = findViewById(R.id.activity_firewall_cursor);
        viewPager = (ViewPager) findViewById(R.id.activity_firewall_viewpager);
        tv_store = (TextView) findViewById(R.id.disk_tv_store);
        tv_system = (TextView) findViewById(R.id.disk_tv_system);
        tv_store.setOnClickListener(new MyOnClickListener(0));
        tv_system.setOnClickListener(new MyOnClickListener(1));

        offset = cursor.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) (screenW / 2.0 - offset);

        fragments = new ArrayList<Fragment>();
        fragments.add(new StoreSpaceFragment());
        fragments.add(new SystemSpaceFragment());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentIndex);
        viewPager.setOnPageChangeListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Animation anim = null;
        switch (position) {
            case 0:
                if(currentIndex ==1) {
                    anim =  new TranslateAnimation(offset,0,0,0);
                }
                break;
            case 1:
                if(currentIndex==0) {
                    anim = new TranslateAnimation(0,offset,0,0);
                }
                break;

        }
        currentIndex = position;
        anim.setDuration(250);
        anim.setFillAfter(true);
        anim.setRepeatCount(0);
        anim.setRepeatMode(Animation.REVERSE);
        cursor.startAnimation(anim);
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
            viewPager.setCurrentItem(index);
        }
    }
}
