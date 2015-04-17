package com.tieto.systemmanagement.authority.controller;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.authority.adapter.MainPagerAdapter;
import com.tieto.systemmanagement.authority.views.TabsLayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Jiang Ping
 */
public class MainPagerFragment extends Fragment {

    public static MainPagerFragment newInstance() {
        return new MainPagerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authority_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ViewPager pager = (ViewPager) getView().findViewById(R.id.pager);
        final TabsLayout tabs = (TabsLayout) getView().findViewById(R.id.tabs);
        tabs.setSegments(new String[] {"程序", "权限"});
        pager.setOnPageChangeListener(tabs.getOnPageChangeListener());
        tabs.setOnTabChangedListener(new TabsLayout.OnTabChangedListener() {
            @Override
            public void onSegmentChanged(TabsLayout view, int selectedIndex) {
                pager.setCurrentItem(selectedIndex, true);
            }
        });
        pager.setAdapter(new MainPagerAdapter(getFragmentManager()));
    }
}
