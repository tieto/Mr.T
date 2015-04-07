package com.tieto.systemmanagement.authority;

import com.tieto.systemmanagement.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class AuthorityActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority);
        getSupportFragmentManager().beginTransaction().
                replace(android.R.id.content, AuthorityAppListFragment.newInstance()).commit();
        //noinspection ConstantConditions
        getActionBar().setIcon(0);
    }
}
