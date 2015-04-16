package com.tieto.systemmanagement.intercept.views;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.intercept.adapter.ConfigCallListViewAdapter;
import com.tieto.systemmanagement.intercept.adapter.ConfigListViewAdapter;
import com.tieto.systemmanagement.intercept.adapter.ConfigMessageListViewAdapter;
import com.tieto.systemmanagement.intercept.service.PhoneFilterServer;
import com.tieto.systemmanagement.intercept.util.InterceptHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageInterceptConfigActivity extends Activity {

    private PhoneFilterServer phoneFilterServer ;
    private ConfigListViewAdapter configListViewAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_intercept_config);

        ListView view = (ListView)findViewById(R.id.intercept_config_management_item);
        configListViewAdapter = new ConfigMessageListViewAdapter(this, getDate());
        view.setAdapter(configListViewAdapter);

        bindService(new Intent(this,PhoneFilterServer.class),serviceConnection, Context.BIND_AUTO_CREATE) ;
    }

    private List<Map<String,Object>> getDate(){

        SharedPreferences sharedPreferences = getSharedPreferences(InterceptHelper.INTERCEPT_CONFIGURATION,MODE_PRIVATE) ;

        boolean isIntercept = sharedPreferences.getBoolean(InterceptHelper.InterceptMessageConfiguration.ENABLE_MESSAGE_INTERCEPT, InterceptHelper.InterceptMessageConfiguration.DEFAULT_ENABLE_MESSAGE_INTERCEPT);
        boolean interceptStrange = sharedPreferences.getBoolean(InterceptHelper.InterceptMessageConfiguration.ENABLE_MESSAGE_INTERCEPT_STRANGE, InterceptHelper.InterceptMessageConfiguration.DEFAULT_ENABLE_MESSAGE_INTERCEPT_STRANGE) ;

        List<Map<String,Object>> date = new ArrayList<Map<String, Object>>() ;
        Map<String,Object> strangeMap = new HashMap<String,Object>() ;
        strangeMap.put("title_enable_intercept",getResources().getString(R.string.title_enable_intercept)) ;
        strangeMap.put("title_is_intercept",getResources().getString(isIntercept ? R.string.intercept : R.string.un_intercept)) ;
        strangeMap.put("intercept",isIntercept) ;
        date.add(strangeMap) ;

        Map<String,Object> anonymousMap = new HashMap<String,Object>() ;
        anonymousMap.put("title_enable_intercept",getResources().getString(R.string.title_message_intercept_strange)) ;
        anonymousMap.put("title_is_intercept",getResources().getString(interceptStrange ? R.string.intercept : R.string.un_intercept)) ;
        anonymousMap.put("intercept",interceptStrange) ;
        date.add(anonymousMap) ;

        return date ;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            phoneFilterServer = ((PhoneFilterServer.myBinder)service).getService();
            configListViewAdapter.setPhoneFilterServer(phoneFilterServer);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            phoneFilterServer = null ;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call_intercept_config, menu);
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
