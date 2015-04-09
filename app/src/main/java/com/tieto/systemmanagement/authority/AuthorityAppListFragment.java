package com.tieto.systemmanagement.authority;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.authority.adapter.AppInfoAdapter;
import com.tieto.systemmanagement.authority.entity.AppWrapper;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiang Ping
 */
public class AuthorityAppListFragment extends ListFragment {

    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private AppInfoAdapter mAdapter;

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
        mAdapter = new AppInfoAdapter(getActivity());
        getListView().setAdapter(mAdapter);
        getListView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mAdapter.setAppListData(AppWrapper.getApplicationList(getActivity(), true));
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

    protected void updateAppList(List<AppWrapper> apps) {
        mAdapter.setAppListData(apps);
    }
}
