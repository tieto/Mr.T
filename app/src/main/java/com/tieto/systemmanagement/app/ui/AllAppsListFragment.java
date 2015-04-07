package com.tieto.systemmanagement.app.ui;


import com.tieto.systemmanagement.app.tools.AppListFragmentTool;

/**
 * Created by jinpei on 3/25/15.SS
 * Fragment of app list.The responsibility of this fragment is that to create
 * a list based on the the
 */
public class AllAppsListFragment extends AppListFragment {
    @Override
    protected int setAppListType() {
        return AppListFragmentTool.APP_LIST_TYPE_ALL;
    }
}
