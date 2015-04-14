package com.tieto.systemmanagement.app.utils.constants;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.app.models.AppModuleModel;
import com.tieto.systemmanagement.app.fragments.AllAppsListFragment;
import com.tieto.systemmanagement.app.fragments.DownloadedAppsListFragment;
import com.tieto.systemmanagement.app.fragments.RunningAppsListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinpei on 25/03/15.
 */
public class AppConstants {

    public static final String PAGE_POSITION = "page_position";

    /**
     * List of app list model
     */
    public static List<AppModuleModel> AppModelList;

    /**
     * Number of viewpager cache.
     */
    public static final int VIEW_PAGER_CACHE_NUM;

    /**
     * Add app list model into mAppModelList.
     */
    static {
        AppModelList = new ArrayList<AppModuleModel>();
        AppModuleModel mAppModuleModel = new AppModuleModel(R.string.app_downloaded, DownloadedAppsListFragment.class);
        AppModelList.add(mAppModuleModel);
        mAppModuleModel = new AppModuleModel(R.string.app_running, RunningAppsListFragment.class);
        AppModelList.add(mAppModuleModel);
        mAppModuleModel = new AppModuleModel(R.string.app_all, AllAppsListFragment.class);
        AppModelList.add(mAppModuleModel);

        VIEW_PAGER_CACHE_NUM = AppModelList.size() - 1;
    }

    public static List<Fragment> getFragmentList() {

        List<Fragment> fragments = new ArrayList<Fragment>();
        int size = AppModelList.size();

        for (int i = 0; i < size; i++) {
            AppModuleModel appModuleModel = AppModelList.get(i);

            try {
                Fragment fragment = appModuleModel.getModuleClass().newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt(PAGE_POSITION, i);
                fragment.setArguments(bundle);
                fragments.add(fragment);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return fragments;
    }

    public static String[] getTitleStrings(Context context) {
        int size = AppModelList.size();
        String[] titles = new String[size];
        for (int i = 0; i < size; i++) {
            titles[i] = context.getString(AppModelList.get(i).getTitle());
        }
        return titles;
    }

}
