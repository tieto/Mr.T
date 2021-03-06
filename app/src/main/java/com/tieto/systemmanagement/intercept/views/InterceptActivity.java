package com.tieto.systemmanagement.intercept.views;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tieto.systemmanagement.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaooked on 4/2/15.
 */
public class InterceptActivity extends FragmentActivity {

    private ViewGroup selectionTitle;
    //default page 0
    private static int DEFAULT_SELECTED_VALUE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercept);

        selectionTitle = (ViewGroup) findViewById(R.id.selection_title);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                setSelectionTitle(position);
            }
        });

        for (int index = 0; index < selectionTitle.getChildCount(); index++) {
            ((TextView) selectionTitle.getChildAt(index)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    viewPager.setCurrentItem(selectionTitle.indexOfChild(view));
                }
            });
        }


        viewPager.setCurrentItem(DEFAULT_SELECTED_VALUE);
        setSelectionTitle(DEFAULT_SELECTED_VALUE);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }


    private void setSelectionTitle(int position) {

        if (selectionTitle != null && position <= selectionTitle.getChildCount()) {
            for (int i = 0; i < selectionTitle.getChildCount(); i++) {
                TextView view = (TextView) selectionTitle.getChildAt(i);
                if (i == position) {
                    view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                    view.setTextColor(getResources().getColor(R.color.vpn_save_mode_text_green));
                    view.setBackgroundColor(getResources().getColor(R.color.common_title_bar_pressed));
                    view.setTag(true);

                    // If view was selected before
                } else if ((view.getTag() != null) && (Boolean.valueOf(view.getTag().toString()))) {
                    view.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                    view.setTextColor(getResources().getColor(R.color.btn_text_pressed));
                    view.setBackgroundColor(getResources().getColor(R.color.grey_disable));
                    view.setTag(false);
                }
            }
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch (i) {
                case 0:
                    return new CallRecordFragment();
                case 1:
                    return new MessageRecordFragment();
                case 2:
                    return new ContentFragmentPager();
            }
            return null;

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "This is a Pager " + position;
        }
    }

    public static class ContentFragmentPager extends Fragment {

        public ContentFragmentPager() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.activity_intercept_pager, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.fragment_content);
            Bundle args = getArguments();


            ListView configurationListView = (ListView) rootView.findViewById(R.id.intercept_configuration);
            configurationListView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            configurationListView.setAdapter(new SimpleAdapter(getActivity(), createData(), R.layout.item_intercept_configuration, new String[]{"content"}, new int[]{R.id.intercept_configuration_item}) {
            });
            configurationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            Intent callIntent = new Intent(getActivity(), CallInterceptConfigActivity.class);
                            startActivity(callIntent);
                            break;
                        case 1:
                            Intent messageIntent = new Intent(getActivity(), MessageInterceptConfigActivity.class);
                            startActivity(messageIntent);
                            break;
                    }
                }
            });
            return rootView;
        }

        private List<Map<String, Object>> createData() {
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

            Map<String, Object> callConfig = new HashMap<String, Object>();
            callConfig.put("content", getResources().getString(R.string.intercept_config_call));
            data.add(callConfig);

            Map<String, Object> messageConfig = new HashMap<String, Object>();
            messageConfig.put("content", getResources().getString(R.string.intercept_config_message));
            data.add(messageConfig);

            return data;
        }

    }
}
