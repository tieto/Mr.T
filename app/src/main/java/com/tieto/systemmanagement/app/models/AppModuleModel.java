package com.tieto.systemmanagement.app.models;

import android.support.v4.app.Fragment;

/**
 * Created by jinpei on 25/03/15.
 * App List Model.Contains layout resource, list title, corresponding fragment.
 */
public class AppModuleModel {

    /**
     * Define the title of list and specified the fragment.
     * The fragment must extends AppListFragment.
     */
    private int title;
    private Class<? extends Fragment> module;

    public AppModuleModel(int title, Class<? extends Fragment> module) {
        this.title = title;
        this.module = module;
    }

    public int getTitle() {
        return title;
    }

    public Class<? extends Fragment> getModuleClass() {
        return module;
    }
}
