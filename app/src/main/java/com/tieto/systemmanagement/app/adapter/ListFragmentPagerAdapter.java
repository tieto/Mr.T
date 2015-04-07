package com.tieto.systemmanagement.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by jinpei on 3/25/15.
 */
public class ListFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private String[] titles;

    public ListFragmentPagerAdapter(FragmentManager fm, List<Fragment> list, String[] titles) {
        super(fm);
        fragments = list;
        this.titles = titles;
    }



    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
