package com.tieto.systemmanagement.app.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.app.adapter.AppListAdapter;
import com.tieto.systemmanagement.app.tools.AppListFragmentTool;

import java.lang.ref.WeakReference;

/**
 * Created by jinpei on 25/03/15.
 */
public abstract class AppListFragment extends Fragment implements View.OnClickListener, ListView.OnItemClickListener {

    /**
     * App List type.
     */
    private int mAppListType;

    /**
     * Position of this page.
     */
    private int mPosition;

    /**
     * Is this fragment still alive.
     */
    public boolean isAlive = true;

    /**
     * App list view.
     */
    private ListView mListView;
    public AppListAdapter mAppListAdapter;
    private ProgressBar mProgressBar;

    /**
     * Handler for filling App list.
     */
    public AppListHandler mAppListHandler;

    /**
     * Instance of app info list tool.
     */
    private AppListFragmentTool mAppListFragmentTool;

    /**
     * Thread for getting appInfo.
     */
    private Thread mAppInfoGetter;

    /**
     * To init the root view.
     */
    protected View initRootView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.app_page,null);
    };

    /**
     * To init Views.
     */
    protected void initView(View root) {
        mListView =  (ListView)root.findViewById(R.id.app_list);
        mProgressBar = (ProgressBar)root.findViewById(R.id.app_list_loading);
        mListView.setOnItemClickListener(this);
    };

    /**
     * Abstract method. To set the type of app list.
     */
    protected abstract int setAppListType();

    /**
     * To get the type of app list.
     */
    public int getType() {
        return mAppListType;
    }

    /**
     * To get the app list info tool.
     */
    public AppListFragmentTool getAppInfoTool() {
        return mAppListFragmentTool == null ? mAppListFragmentTool = new AppListFragmentTool(this) : mAppListFragmentTool;
    }

    /**
     * Init App list handler.
     */
    protected void initAppListHandler() {
        mAppListHandler = new AppListHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isAlive = true;
        View body = initRootView(inflater);
        initView(body);
        mAppListType = setAppListType();
        initAppListHandler();
        return body;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        /**
         * New a thread to fill the list with app info.
         */
        if(mListView != null && mAppListAdapter == null && isAlive) {
            mAppInfoGetter = new Thread(new AppListThread());
            mAppInfoGetter.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAlive = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Called after loading.
     */
    private void switchView() {
        if(View.VISIBLE == mProgressBar.getVisibility()) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppListAdapter.AppDetailIntent intent = mAppListAdapter.new AppDetailIntent(position);
        intent.setClass(this.getActivity(),AppDetailActivity.class);
        startActivity(intent);
    }

    /**
     * Thread for get app list adapter.
     */
    private class AppListThread implements Runnable {
        @Override
        public void run() {
            if(!isAlive) {
                return;
            }
            /**
             * Call getAppListAdapter method to get the adapter.
             */
            mAppListAdapter = getAppInfoTool().getAppListAdapter();

            Message msg = new Message();
            msg.what = AppListHandler.SET_APP_LIST_ADAPTER;
            mAppListHandler.sendMessage(msg);
        }
    }

    /**
     * Update the data of app list view.
     */
    public static class AppListHandler extends Handler {

        /**
         * Set app list adapter.
         */
        public static final int SET_APP_LIST_ADAPTER = 0;

        private WeakReference<AppListFragment> mAppListFragment;

        public AppListHandler(AppListFragment appListFragment) {
            this.mAppListFragment = new WeakReference(appListFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            AppListFragment appListFragment = mAppListFragment.get();

            switch (msg.what) {
                case SET_APP_LIST_ADAPTER:
                    if (appListFragment != null && appListFragment.isAlive) {
                        appListFragment.mListView.setAdapter(appListFragment.mAppListAdapter);
                        appListFragment.mAppListAdapter.beginToGetSize();
                        appListFragment.switchView();
                    }
                    break;
            }
        }
    }

}
