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
import com.tieto.systemmanagement.intercept.service.PhoneFilterServer;
import com.tieto.systemmanagement.intercept.util.InterceptHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallInterceptConfigActivity extends Activity {

    private PhoneFilterServer phoneFilterServer ;
    private ConfigListViewAdapter configListViewAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_intercept_config);

        ListView view = (ListView)findViewById(R.id.intercept_config_management_item);
        configListViewAdapter = new ConfigCallListViewAdapter(this, prepareConfigDate());
        view.setAdapter(configListViewAdapter);

        bindService(new Intent(this,PhoneFilterServer.class),serviceConnection, Context.BIND_AUTO_CREATE) ;

    }

    private List<Map<String,Object>> prepareConfigDate(){

        SharedPreferences sharedPreferences = getSharedPreferences(InterceptHelper.INTERCEPT_CONFIGURATION,MODE_PRIVATE) ;

        boolean isIntercept = sharedPreferences.getBoolean(InterceptHelper.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT, InterceptHelper.InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT);
        boolean interceptStrange = sharedPreferences.getBoolean(InterceptHelper.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_STRANGE, InterceptHelper.InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT_STRANGE) ;
        boolean interceptContract = sharedPreferences.getBoolean(InterceptHelper.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_CONTRACT, InterceptHelper.InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT_CONTRACT) ;
        boolean interceptAnonymity = sharedPreferences.getBoolean(InterceptHelper.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_ANONYMITY, InterceptHelper.InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT_ANONYMITY) ;

        List<Map<String,Object>> date = new ArrayList<Map<String, Object>>() ;
        Map<String,Object> isInterceptMap = new HashMap<String,Object>() ;
        isInterceptMap.put("title_call_intercept",getResources().getString(R.string.title_enable_intercept)) ;
        isInterceptMap.put("title_is_intercept",getResources().getString(isIntercept ? R.string.intercept : R.string.un_intercept)) ;
        isInterceptMap.put("intercept",isIntercept) ;
        date.add(isInterceptMap) ;

        Map<String,Object> strangeMap = new HashMap<String,Object>() ;
        strangeMap.put("title_call_intercept",getResources().getString(R.string.title_call_intercept_strange)) ;
        strangeMap.put("title_is_intercept",getResources().getString(interceptStrange ? R.string.intercept : R.string.un_intercept)) ;
        strangeMap.put("intercept",interceptStrange) ;
        date.add(strangeMap) ;

        Map<String,Object> anonymousMap = new HashMap<String,Object>() ;
        anonymousMap.put("title_call_intercept",getResources().getString(R.string.title_call_intercept_anonymous)) ;
        anonymousMap.put("title_is_intercept",getResources().getString(interceptAnonymity ? R.string.intercept : R.string.un_intercept)) ;
        anonymousMap.put("intercept",interceptAnonymity) ;
        date.add(anonymousMap) ;

        Map<String,Object> contractMap = new HashMap<String,Object>() ;
        contractMap.put("title_call_intercept",getResources().getString(R.string.title_call_intercept_contract)) ;
        contractMap.put("title_is_intercept",getResources().getString(interceptContract ? R.string.intercept : R.string.un_intercept)) ;
        contractMap.put("intercept",interceptContract) ;
        date.add(contractMap) ;

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
