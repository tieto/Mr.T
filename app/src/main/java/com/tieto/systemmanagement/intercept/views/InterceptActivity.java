package com.tieto.systemmanagement.intercept.views;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.tieto.systemmanagement.R;

/**
 * Created by zhaooked on 4/2/15.
 */
public class InterceptActivity extends FragmentActivity {

    private ViewGroup selectionTitle ;
    //default page 0
    private static int DEFAULT_SELECTED_VALUE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercept);

        selectionTitle = (ViewGroup)findViewById(R.id.selection_title) ;

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager) ;
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {
                setSelectionTitle(position);
            }
        }) ;

        for(int index = 0; index < selectionTitle.getChildCount() ; index ++){
            ((TextView)selectionTitle.getChildAt(index)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    viewPager.setCurrentItem(selectionTitle.indexOfChild(view));
                }
            });
        }


        viewPager.setCurrentItem(DEFAULT_SELECTED_VALUE);
        setSelectionTitle(DEFAULT_SELECTED_VALUE) ;

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }


    private void setSelectionTitle(int position){

        if(selectionTitle!=null && position <= selectionTitle.getChildCount()){
            for(int i = 0 ;i< selectionTitle.getChildCount();i++){
                TextView view = (TextView)selectionTitle.getChildAt(i) ;
                if (i==position) {
                    view.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
                    view.setTextColor(getResources().getColor(R.color.vpn_save_mode_text_green));
                    view.setBackgroundColor(getResources().getColor(R.color.common_title_bar_pressed));
                    view.setTag(true);

                // If view was selected before
                }else if((view.getTag() !=null) && (Boolean.valueOf(view.getTag().toString()))) {
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
            Fragment fragment = new ContentFragmentPager() ;
            Bundle args = new Bundle();
            args.putInt(ContentFragmentPager.SELECT_PAGE,i+1);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "This is a Pager " + position ;
        }
    }

    public static class ContentFragmentPager extends Fragment {

        private static String SELECT_PAGE = "selected_page" ;

        public ContentFragmentPager(){
        }

        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.activity_intercept_pager,container,false) ;
            TextView textView = (TextView)rootView.findViewById(R.id.fragment_content) ;
            Button singleButton = (Button)rootView.findViewById(R.id.left_button) ;
            Button deleteButton = (Button)rootView.findViewById(R.id.right_button) ;
            Bundle args = getArguments() ;
            int pagerIndex = args.getInt(SELECT_PAGE);

            switch (pagerIndex){

                case 1 :
                    singleButton.setText("标记");
                    deleteButton.setText("删除");
                    break;
                case 2 :


                    break;
            }
            textView.setText("Selected Page " + pagerIndex);
            return rootView ;
        }

    }
}
