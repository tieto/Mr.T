package com.tieto.systemmanagement.authority;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.authority.adapter.AppInfoAdapter;
import com.tieto.systemmanagement.authority.entity.AppInfo;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiang Ping
 */
public class AuthorityFragment extends ListFragment {

    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private AppInfoAdapter mAdapter;

    public static AuthorityFragment newInstance() {
        AuthorityFragment fragment = new AuthorityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AuthorityFragment() {
        // Reserved for system
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authority, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new AppInfoAdapter(getActivity());
        getListView().setAdapter(mAdapter);
        new AppLoader(this).executeOnExecutor(mExecutor, null);

        Animation fade = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        fade.setDuration(500);
        LayoutAnimationController lac = new LayoutAnimationController(fade);
        lac.setDelay(0.1f);
        getListView().setLayoutAnimation(lac);

        View empty = getView().findViewById(R.id.empty_view);
        getListView().setEmptyView(empty);
    }

    protected void updateAppList(List<AppInfo> apps) {
        mAdapter.setAppListData(apps);
    }

    private static class AppLoader extends AsyncTask<Void, Void, List<AppInfo>> {

        private WeakReference<AuthorityFragment> mContextRef;

        public AppLoader(AuthorityFragment fragment) {
            mContextRef = new WeakReference<AuthorityFragment>(fragment);
        }

        @Override
        protected List<AppInfo> doInBackground(Void... voids) {
            return AppInfo.getApplicationList(mContextRef.get().getActivity());
        }

        @Override
        protected void onPostExecute(List<AppInfo> apps) {
            super.onPostExecute(apps);
            AuthorityFragment fragment = mContextRef.get();
            if (fragment != null && fragment.isAdded()) {
                fragment.updateAppList(apps);
            }
        }
    }
}
