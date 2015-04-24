package com.tieto.systemmanagement.authority.controller;

import android.app.AppOpsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.authority.entity.AppPermission;
import com.tieto.systemmanagement.authority.entity.AppWrapper;
import com.tieto.systemmanagement.authority.entity.BitmapWorker;
import com.tieto.systemmanagement.authority.model.PermissionManager;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jiang Ping
 */
public class AuthorityDetailFragment extends Fragment {

    private ImageView mImageIcon;
    private AppWrapper mAppInfo;
    private ListView mPermissionListView;
    private LayoutInflater mInflater;

    public static AuthorityDetailFragment newInstance() {
        return new AuthorityDetailFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("app_info", mAppInfo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        return inflater.inflate(R.layout.fragment_authority_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mAppInfo = args.getParcelable("app_info");
        }
        if (savedInstanceState != null) {
            mAppInfo = savedInstanceState.getParcelable("app_info");
        }

        if(mAppInfo == null)
        {
            return;
        }

        //set app title
        TextView name = (TextView) getView().findViewById(R.id.text_app_name);
        name.setText(mAppInfo.getName(getActivity()));

        //set app icon
        mImageIcon = (ImageView) getView().findViewById(R.id.image_app_icon);
        Drawable icon = mAppInfo.loadIcon(getActivity());

        BitmapWorker bw = new BitmapWorker(icon);
        if (bw.getBitmap() != null) {
            mImageIcon.setImageBitmap(bw.createReflectBitmap(0.5f));
        }

        //set permissions
        mPermissionListView = (ListView)getView().findViewById(R.id.listview_permission_list);
        ArrayList<AppPermission> appPermissions = getPermissionByAppInfo(mAppInfo);
        PermissionAdapter permissionAdapter = new PermissionAdapter(appPermissions);
        mPermissionListView.setAdapter(permissionAdapter);
    }

    private ArrayList<AppPermission> getPermissionByAppInfo(AppWrapper mAppInfo) {
        List<Object> listPackageOps = mAppInfo.getPermissionInfos();
        if(listPackageOps == null)
        {
            return null;
        }

        PermissionManager permissionManager = PermissionManager.getInstance();
        if(permissionManager != null) {
            ArrayList<AppPermission> appPermissions
                    = permissionManager.getPermissionByPackage(listPackageOps
                                , mAppInfo.getApplicationInfo());
            return appPermissions;
        }
        return  null;
    }

    private  class PermissionAdapter extends BaseAdapter
    {
        ArrayList<AppPermission> mData = null;
        PermissionAdapter(ArrayList<AppPermission> appPermissions)
        {
            mData = appPermissions;
        }

        @Override
        public int getCount() {
            return mData == null? 0 : mData.size();
        }

        @Override
        public AppPermission getItem(int pos) {
            return mData == null? null : mData.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        private final class ConvertHolder {
            private TextView name;
            private Switch permissionEnable;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup viewGroup) {
            ConvertHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_permission, viewGroup, false);
                holder = new ConvertHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name_permission);
                holder.permissionEnable = (Switch) convertView.findViewById(R.id.switch_permission);
                convertView.setTag(holder);
            } else {
                holder = (ConvertHolder) convertView.getTag();
            }
            final  AppPermission permission = mData.get(pos);
            holder.name.setText(permission.label);
            holder.permissionEnable.setChecked(permission.mode == AppOpsManager.MODE_IGNORED? false : true );
            holder.permissionEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setPermission(permission,buttonView, isChecked);
                }
            });
            return convertView;
        }
    }

    private void setPermission(AppPermission permission,CompoundButton buttonView,  boolean isChecked) {
        PermissionManager permissionManager = PermissionManager.getInstance();
        boolean isSetOk = permissionManager.setPermission(permission, isChecked);
        if(!isSetOk)
        {
            Toast.makeText(getActivity(), R.string.can_not_set_permission, Toast.LENGTH_SHORT).show();
            buttonView.setChecked(!isChecked);
        }
    }

}
