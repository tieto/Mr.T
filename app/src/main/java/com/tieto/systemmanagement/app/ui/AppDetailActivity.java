package com.tieto.systemmanagement.app.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.app.adapter.AppListAdapter;
import com.tieto.systemmanagement.app.constants.AppListCache;
import com.tieto.systemmanagement.app.model.AppListItemModel;
import com.tieto.systemmanagement.app.model.AppSizeModel;
import com.tieto.systemmanagement.app.tools.ApplicationsState;

public class AppDetailActivity extends ActionBarActivity implements ApplicationsState.Callbacks {

    /**
     * Package name from intent.
     */
    private String packageName;

    private TextView app_detail_name;
    private TextView app_detail_version;
    private TextView app_detail_total_size;
    private TextView app_detail_application_size;
    private TextView app_detail_data_size;
    private TextView app_detail_cache_size;

    private ImageView app_detail_icon;

    private Button app_detail_button_force_stop;
    private Button app_detail_button_uninstall;
    private Button app_detail_button_clear_data;
    private Button app_detail_button_clear_cache;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);

        packageName = getPackageNameFromIntent(getIntent());

        app_detail_name = (TextView)findViewById(R.id.app_detail_name);
        app_detail_version = (TextView)findViewById(R.id.app_detail_version);
        app_detail_total_size = (TextView)findViewById(R.id.app_detail_total_size);
        app_detail_application_size = (TextView)findViewById(R.id.app_detail_application_size);
        app_detail_data_size = (TextView)findViewById(R.id.app_detail_data_size);
        app_detail_cache_size = (TextView)findViewById(R.id.app_detail_cache_size);

        app_detail_icon = (ImageView)findViewById(R.id.app_detail_icon);

        init();

        ApplicationsState.setOnStateChanged(packageName,this);
    }

    private void init() {
        PackageManager pm = getPackageManager();
        AppListItemModel appListItemModel = getPackageInfo(packageName);

        if(appListItemModel != null) {
            app_detail_icon.setImageDrawable(appListItemModel.getIcon());
            app_detail_name.setText(appListItemModel.getAppLabel());
            app_detail_version.setText(appListItemModel.getVersionName());

            if (appListItemModel.isSizeSet()) {
                app_detail_total_size.setText(String.valueOf(appListItemModel.getTotalSize()));
                app_detail_application_size.setText(String.valueOf(appListItemModel.getProgramSize()));
                app_detail_data_size.setText(String.valueOf(appListItemModel.getDataSize()));
                app_detail_cache_size.setText(String.valueOf(appListItemModel.getCacheSize()));
            }
        }
    }

    private AppListItemModel getPackageInfo(String packageName) {
        AppListItemModel appListItemModel = AppListCache.AppListItemModelCache.get(packageName);
        if(appListItemModel != null) {
            return appListItemModel;
        }

        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName,PackageManager.GET_UNINSTALLED_PACKAGES);
            appListItemModel = new AppListItemModel(packageInfo,pm);
            return appListItemModel;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPackageNameFromIntent(Intent intent) {
        return intent.getExtras().getString(AppListAdapter.AppDetailIntent.APP_PACKAGE_NAME);
    }

    @Override
    public void onRunningStateChanged(boolean running) {

    }

    @Override
    public void onPackageIconChanged() {

    }

    @Override
    public void onPackageSizeChanged(AppSizeModel appSizeModel) {
        app_detail_total_size.setText(String.valueOf(appSizeModel.getTotalSize()));
        app_detail_application_size.setText(String.valueOf(appSizeModel.getProgramSize()));
        app_detail_data_size.setText(String.valueOf(appSizeModel.getDataSize()));
        app_detail_cache_size.setText(String.valueOf(appSizeModel.getCacheSize()));
    }
}
