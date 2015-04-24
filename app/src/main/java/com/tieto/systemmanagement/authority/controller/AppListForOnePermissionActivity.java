package com.tieto.systemmanagement.authority.controller;

import android.app.AppOpsManager;
import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.authority.entity.AppPermission;
import com.tieto.systemmanagement.authority.model.PermissionManager;

import java.util.ArrayList;

public class AppListForOnePermissionActivity extends ListActivity {

    ArrayList<Object> mAppList;
    LayoutInflater mInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            savedInstanceState.putSerializable("app_list", mAppList);
        }
        else{
            mAppList = PermissionManager.getInstance().appList;
        }
        setContentView(R.layout.activity_app_list_for_one_permission);
        initUI();
    }

    private void initUI() {
        mInflater = getLayoutInflater();
        ApplistAdapter appListAdapter = new ApplistAdapter(mAppList);
        getListView().setAdapter(appListAdapter);
    }

    private  class ApplistAdapter extends BaseAdapter
    {
        ArrayList<Object> mData = null;
        ApplistAdapter(ArrayList<Object> appPermissions)
        {
            mData = appPermissions;
        }

        @Override
        public int getCount() {
            return mData == null? 0 : mData.size();
        }

        @Override
        public Object getItem(int pos) {
            return mData == null? null : mData.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return 0;
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
            
            final  Object object = mData.get(pos);
            holder.name.setText(PermissionManager.getInstance().getAppName(object));
            holder.permissionEnable.setChecked(PermissionManager.getInstance().getAppPerMissionIsEnable(object));
            holder.permissionEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setPermission(object, buttonView, isChecked);
                }
            });
            return convertView;
        }
    }

    private void setPermission(Object object, CompoundButton buttonView, boolean isChecked) {
        PermissionManager permissionManager = PermissionManager.getInstance();
        boolean isSetOk = permissionManager.setPermissionByObject(object, isChecked);
        if(!isSetOk)
        {
            Toast.makeText(this, R.string.can_not_set_permission, Toast.LENGTH_SHORT).show();
            buttonView.setChecked(!isChecked);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_list_for_one_permission, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
