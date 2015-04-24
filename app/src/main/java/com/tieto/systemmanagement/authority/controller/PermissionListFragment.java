package com.tieto.systemmanagement.authority.controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.authority.entity.AppPermission;
import com.tieto.systemmanagement.authority.entity.AppWrapper;
import com.tieto.systemmanagement.authority.model.PermissionManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jiang Ping
 */
public class PermissionListFragment extends Fragment {
    private Adapter mAdapter;

    public static PermissionListFragment newInstance() {
        return new PermissionListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authority_permission_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list = (ListView) getView().findViewById(android.R.id.list);

        //TODO:use async to permission list
        final ArrayMap<String,ArrayList<Object>> permissionList = getPermissonList();
        mAdapter = new Adapter(getActivity(),permissionList);
        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getActivity(), AppListForOnePermissionActivity.class);
                intent.putExtra("app_list", mAdapter.getItem(pos));

                //TODO: use bundle transfer data
                PermissionManager.getInstance().appList = mAdapter.getItem(pos);
                PermissionManager.getInstance().permissionName = permissionList.keyAt(pos);
                startActivity(intent);
            }
        });

    }

    private ArrayMap<String,ArrayList<Object>> getPermissonList() {
        ArrayMap<String,ArrayList<Object>> permissionList = new ArrayMap<String,ArrayList<Object>>();

        PermissionManager permissionManager = PermissionManager.getInstance();
        List<Object> listPackageOps =  permissionManager.getAllPermissionpackageOps();
        for (Object packageOps: listPackageOps) {
            ArrayList<AppPermission> appPermissions = permissionManager.getAppPermissionByPackage(packageOps, null);
            for(AppPermission appPermission:appPermissions)
            {
                ArrayList<Object> applist = permissionList.get(appPermission.label);
                if(applist == null)
                {
                    applist = new ArrayList<Object>();
                }
                applist.add(packageOps);
                permissionList.put(appPermission.label,applist);
            }
        }
        return permissionList;
    }

    private static class Adapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private ArrayMap<String,ArrayList<Object>> mList;
        public Adapter(Context context,ArrayMap<String,ArrayList<Object>> permissionList ) {
            mInflater = LayoutInflater.from(context);
            mList = permissionList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public ArrayList<Object> getItem(int i) {
            String key = mList.keyAt(i);
            return mList.get(key);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = mInflater.inflate(R.layout.authority_permission_list_item, viewGroup, false);
                holder = new ViewHolder();
                holder.title = (TextView) view.findViewById(R.id.title);
                holder.description = (TextView) view.findViewById(R.id.description);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            String key = mList.keyAt(i);
            holder.title.setText(key);
            ArrayList<Object> objects = mList.get(key);
            holder.description.setText(String.format("%d个App" , objects.size()));
            return view;
        }
    }

    private final static class ViewHolder {
        TextView title;
        TextView description;
    }
}
