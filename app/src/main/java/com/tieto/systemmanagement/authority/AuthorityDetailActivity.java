package com.tieto.systemmanagement.authority;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tieto.systemmanagement.R;

public class AuthorityDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority);
        Fragment fragment = AuthorityDetailFragment.newInstance();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().
                replace(android.R.id.content, fragment).commit();
        //noinspection ConstantConditions
        getActionBar().setIcon(0);
    }
}
