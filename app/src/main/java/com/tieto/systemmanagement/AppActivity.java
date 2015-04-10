package com.tieto.systemmanagement;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import com.tieto.systemmanagement.app.adapter.ListFragmentPagerAdapter;
import com.tieto.systemmanagement.app.constants.AppConstants;
import com.tieto.systemmanagement.app.constants.AppListCache;

import java.util.List;

/**
 * Created by jinpei on 25/03/15.
 * Main activity of app management.
 */
public class AppActivity extends FragmentActivity {

    /**
     * Position of default page.
     */
    private final int mDefaultPage = 0;

    /**
     * Contain all lists of app management.
     */
    private ViewPager mViewPager;

    /**
     * Title of ViewPager.
     */
    private PagerTabStrip mPagerTabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        LayoutInflater mInflater = getLayoutInflater();
        List<Fragment> fragments = AppConstants.getFragmentList();
        String[] titles = AppConstants.getTitleStrings(this);

        ListFragmentPagerAdapter mListFragmentPagerAdapter = new ListFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles);

        mViewPager = (ViewPager) findViewById(R.id.appManagement_ViewPager);
        mViewPager.setAdapter(mListFragmentPagerAdapter);
        mViewPager.setCurrentItem(mDefaultPage);
        mViewPager.setOffscreenPageLimit(AppConstants.VIEW_PAGER_CACHE_NUM);

        mPagerTabStrip = (PagerTabStrip)findViewById(R.id.appManagement_tabs);
        mPagerTabStrip.setClickable(true);
        mPagerTabStrip.setDrawFullUnderline(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppListCache.clearCache();
    }

    /**
     * Switch ViewPager by item's position.
     */
    public void switchViewPager(int item) {
        mViewPager.setCurrentItem(item);
    }

}
