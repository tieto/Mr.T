package com.tieto.systemmanagement.authority.controller;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.authority.adapter.AppListAdapter;
import com.tieto.systemmanagement.authority.entity.AppWrapper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang Ping
 */
public class AuthorityAppListFragment extends ListFragment {

    private AppListAdapter mAdapter;

    public static AuthorityAppListFragment newInstance() {
        AuthorityAppListFragment fragment = new AuthorityAppListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AuthorityAppListFragment() {
        // Reserved for system
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authority_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new AppListAdapter(getActivity());
        getListView().setAdapter(mAdapter);
        getListView().getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mAdapter.setAppListData(getApplicationList(getActivity(), true));
                getListView().getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

        View empty = getView().findViewById(R.id.empty_view);
        getListView().setEmptyView(empty);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getActivity(), AuthorityDetailActivity.class);
                intent.putExtra("app_info", mAdapter.getItem(pos));
                startActivity(intent);
            }
        });
    }

    private List<AppWrapper> getApplicationList(Context context, boolean hasSystem) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfo = pm.getInstalledPackages(
                PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        List<AppWrapper> appList = new ArrayList<AppWrapper>();
        for (final PackageInfo pkg : packageInfo) {
            ApplicationInfo app = pkg.applicationInfo;

            // Ignore the system app
            if (!hasSystem && (app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }

            if (app != null) {
                AppWrapper appWrapper = new AppWrapper(pkg);
                appList.add(appWrapper);
            }
        }
        return appList;
    }
}
