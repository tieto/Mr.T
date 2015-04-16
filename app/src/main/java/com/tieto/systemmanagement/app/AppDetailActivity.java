package com.tieto.systemmanagement.app;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.app.adapters.AppListAdapter;
import com.tieto.systemmanagement.app.models.AppInfoModel;
import com.tieto.systemmanagement.app.models.AppSizeModel;
import com.tieto.systemmanagement.app.models.ApplicationsState;
import com.tieto.systemmanagement.app.utils.AppManagementTool;
import com.tieto.systemmanagement.app.utils.constants.AppListCache;

public class AppDetailActivity extends FragmentActivity implements ApplicationsState.Callbacks, View.OnClickListener {

    /**
     * Request code.
     */
    public static final int REQUEST_UNINSTALL = 1;

    /**
     * Result code.
     */
    public static final int RESULT_NO_CHANGE = RESULT_OK;
    public static final int RESULT_PACKAGE_STATE_CHANGED = 2;
    public static final int RESULT_RUNNING_STATE_CHANGED = 3;

    /**
     * Package name from intent.
     */
    private String packageName;

    private AppInfoModel mAppInfoModel;

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

        app_detail_name = (TextView) findViewById(R.id.app_detail_name);
        app_detail_version = (TextView) findViewById(R.id.app_detail_version);
        app_detail_total_size = (TextView) findViewById(R.id.app_detail_total_size);
        app_detail_application_size = (TextView) findViewById(R.id.app_detail_application_size);
        app_detail_data_size = (TextView) findViewById(R.id.app_detail_data_size);
        app_detail_cache_size = (TextView) findViewById(R.id.app_detail_cache_size);

        app_detail_button_force_stop = (Button) findViewById(R.id.app_detail_button_force_stop);
        app_detail_button_uninstall = (Button) findViewById(R.id.app_detail_button_uninstall);
        app_detail_button_clear_data = (Button) findViewById(R.id.app_detail_button_clear_data);
        app_detail_button_clear_cache = (Button) findViewById(R.id.app_detail_button_clear_cache);

        app_detail_icon = (ImageView) findViewById(R.id.app_detail_icon);

        init();

        ApplicationsState.setOnStateChanged(packageName, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppInfoModel = null;
        ApplicationsState.getInstance().removeOnStateChanged(packageName);
    }

    private void init() {
        mAppInfoModel = getPackageInfo(packageName);

        if (mAppInfoModel != null) {
            app_detail_icon.setImageDrawable(mAppInfoModel.getIcon());
            app_detail_name.setText(mAppInfoModel.getAppLabel());
            app_detail_version.setText(mAppInfoModel.getVersionName());

            if (mAppInfoModel.isSizeSet()) {
                app_detail_total_size.setText(String.valueOf(mAppInfoModel.getTotalSize()));
                app_detail_application_size.setText(String.valueOf(mAppInfoModel.getProgramSize()));
                app_detail_data_size.setText(String.valueOf(mAppInfoModel.getDataSize()));
                app_detail_cache_size.setText(String.valueOf(mAppInfoModel.getCacheSize()));
            }

            if(mAppInfoModel.getUID() == -1) {
                app_detail_button_force_stop.setVisibility(View.INVISIBLE);
            }

            if((mAppInfoModel.getFlag() & ApplicationInfo.FLAG_INSTALLED) == 0) {
                app_detail_button_uninstall.setEnabled(false);
            }
        } else {
            Toast.makeText(this,R.string.app_detail_failure_to_get_package_info,Toast.LENGTH_LONG);
            finish();
        }

        app_detail_button_force_stop.setOnClickListener(this);
        app_detail_button_uninstall.setOnClickListener(this);
        app_detail_button_clear_data.setOnClickListener(this);
        app_detail_button_clear_cache.setOnClickListener(this);
    }

    private AppInfoModel getPackageInfo(String packageName) {
        AppInfoModel appInfoModel = AppListCache.AppListItemModelCache.get(packageName);
        if (appInfoModel != null) {
            return appInfoModel;
        }
        return null;
    }

    private boolean checkPackage() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            if(packageInfo != null)
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private String getPackageNameFromIntent(Intent intent) {
        return intent.getExtras().getString(AppListAdapter.AppDetailIntent.APP_PACKAGE_NAME);
    }

    @Override
    public void onRunningStateChanged(boolean running) {

    }

    @Override
    public void onPackageStateChanged() {
        if(!checkPackage()) {
            Intent intent = getIntent();
            intent.putExtra(AppListAdapter.AppDetailIntent.APP_PACKAGE_NAME, packageName);
            setResult(RESULT_PACKAGE_STATE_CHANGED, intent);
            finish();
        }
    }

    @Override
    public void onPackageSizeChanged(AppSizeModel appSizeModel) {
        app_detail_total_size.setText(String.valueOf(appSizeModel.getTotalSize()));
        app_detail_application_size.setText(String.valueOf(appSizeModel.getProgramSize()));
        app_detail_data_size.setText(String.valueOf(appSizeModel.getDataSize()));
        app_detail_cache_size.setText(String.valueOf(appSizeModel.getCacheSize()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_detail_button_force_stop:
                AppManagementTool.ForceStop(this, packageName);
                break;
            case R.id.app_detail_button_uninstall:
                AppManagementTool.Uninstall(this, packageName);
                break;
            case R.id.app_detail_button_clear_data:
                AppManagementTool.ClearData(this, packageName);
                break;
            case R.id.app_detail_button_clear_cache:
                AppManagementTool.ClearCache(this, packageName);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_UNINSTALL:
                ApplicationsState.getInstance().stateChanged(packageName);
                break;
        }
    }
}
