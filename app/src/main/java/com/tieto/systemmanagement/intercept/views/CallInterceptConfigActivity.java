package com.tieto.systemmanagement.intercept.views;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.intercept.adapter.ConfigListViewAdapter;
import com.tieto.systemmanagement.intercept.util.InterceptConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallInterceptConfigActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_intercept_config);

        ListView view = (ListView)findViewById(R.id.intercept_config_management_item);
        view.setAdapter(new ConfigListViewAdapter(this,getDate()));

        setTitle("电话拦截设置");
    }

    private List<Map<String,Object>> getDate(){

        SharedPreferences sharedPreferences = getSharedPreferences(InterceptConfiguration.INTERCEPT_CONFIGURATION,MODE_PRIVATE) ;

        boolean isIntercept = sharedPreferences.getBoolean(InterceptConfiguration.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT, InterceptConfiguration.InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT);
        boolean interceptContract = sharedPreferences.getBoolean(InterceptConfiguration.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_CONTRACT, InterceptConfiguration.InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT_CONTRACT) ;
        boolean interceptAnonymity = sharedPreferences.getBoolean(InterceptConfiguration.InterceptCallConfiguration.ENABLE_CALL_INTERCEPT_ANONYMITY, InterceptConfiguration.InterceptCallConfiguration.DEFAULT_ENABLE_CALL_INTERCEPT_ANONYMITY) ;

        List<Map<String,Object>> date = new ArrayList<Map<String, Object>>() ;
        Map<String,Object> strangeMap = new HashMap<String,Object>() ;
        strangeMap.put("title_call_intercept",getResources().getString(R.string.title_call_intercept_strange)) ;
        strangeMap.put("title_is_intercept",getResources().getString(isIntercept ? R.string.intercept : R.string.un_intercept)) ;
        strangeMap.put("intercept",isIntercept) ;
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
