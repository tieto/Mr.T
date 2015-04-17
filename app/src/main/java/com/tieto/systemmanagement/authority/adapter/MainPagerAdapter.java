package com.tieto.systemmanagement.authority.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tieto.systemmanagement.authority.controller.AuthorityAppListFragment;
import com.tieto.systemmanagement.authority.controller.PermissionListFragment;

/**
 * @author Jiang Ping
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private static Fragment[] sFragments = {
            AuthorityAppListFragment.newInstance(),
            PermissionListFragment.newInstance()
    };

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return sFragments[position];
    }

    @Override
    public int getCount() {
        return sFragments.length;
    }
}
