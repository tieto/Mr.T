package com.tieto.systemmanagement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.tieto.systemmanagement.trafficmonitor.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class BasicTabbedActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{

    //TODO:the class member var need start m
    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TextView tv_tab_1;
    private TextView tv_tab_2;
    private View cursor;

    private int currentIndex = 0;
    private long offset = 0;

    @Override
    protected void onResume() {
        super.onResume();
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

        if (anim==null)return;

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

    public void set2Tabs(List<Class<? extends Fragment>>fs,List<String> ts) throws IllegalAccessException, InstantiationException {
        if (fs.size() !=2 || ts.size() !=2) {
            throw new IllegalArgumentException("This class only support tabs with 2 fragments");
        }

        Fragment tab1Fragment = fs.get(0).newInstance();
        Fragment tab2Fragment = fs.get(1).newInstance();

        cursor = findViewById(R.id.tabbed_cursor);
        viewPager = (ViewPager) findViewById(R.id.tabbed_viewpager);
        tv_tab_1 = (TextView) findViewById(R.id.tab_1);
        tv_tab_2 = (TextView) findViewById(R.id.tab_2);
        tv_tab_1.setOnClickListener(new MyOnClickListener(0));
        tv_tab_2.setOnClickListener(new MyOnClickListener(1));
        tv_tab_1.setText(ts.get(0).toString());
        tv_tab_2.setText(ts.get(1).toString());

        offset = cursor.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) (screenW / 2.0 - offset);

        fragments = new ArrayList<Fragment>();
        fragments.add(tab1Fragment);
        fragments.add(tab2Fragment);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentIndex);
        viewPager.setOnPageChangeListener(this);
    }
}
