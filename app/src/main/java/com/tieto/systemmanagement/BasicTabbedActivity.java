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

    private ArrayList<Fragment> mFrags;
    private ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;
    private TextView mTextviewTab1;
    private TextView mTextviewTab2;
    private View mCursor;

    private int mCurrIndex = 0;
    private long mOffset = 0;

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
                if(mCurrIndex ==1) {
                    anim =  new TranslateAnimation(mOffset,0,0,0);
                }
                break;
            case 1:
                if(mCurrIndex ==0) {
                    anim = new TranslateAnimation(0, mOffset,0,0);
                }
                break;

        }
        mCurrIndex = position;

        if (anim==null)return;

        anim.setDuration(250);
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

    public void set2Tabs(List<Class<? extends Fragment>>fs,List<String> ts) throws IllegalAccessException, InstantiationException {
        if (fs.size() !=2 || ts.size() !=2) {
            throw new IllegalArgumentException("This class only support tabs with 2 mFrags");
        }

        Fragment tab1Fragment = fs.get(0).newInstance();
        Fragment tab2Fragment = fs.get(1).newInstance();

        mCursor = findViewById(R.id.tabbed_cursor);
        mViewPager = (ViewPager) findViewById(R.id.tabbed_viewpager);
        mTextviewTab1 = (TextView) findViewById(R.id.tab_1);
        mTextviewTab2 = (TextView) findViewById(R.id.tab_2);
        mTextviewTab1.setOnClickListener(new MyOnClickListener(0));
        mTextviewTab2.setOnClickListener(new MyOnClickListener(1));
        mTextviewTab1.setText(ts.get(0).toString());
        mTextviewTab2.setText(ts.get(1).toString());

        mOffset = mCursor.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        mOffset = (int) (screenW / 2.0 - mOffset);

        mFrags = new ArrayList<Fragment>();
        mFrags.add(tab1Fragment);
        mFrags.add(tab2Fragment);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFrags);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrIndex);
        mViewPager.setOnPageChangeListener(this);
    }
}
