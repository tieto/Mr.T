package com.tieto.systemmanagement.authority.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.tieto.systemmanagement.R;

/**
 * @author Jiang Ping
 */
public abstract class AbsFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_fragment_base);
        getSupportFragmentManager().beginTransaction().
                replace(android.R.id.content, onCreateFragment(savedInstanceState)).commit();
        if (getActionBar() != null) {
            getActionBar().setIcon(0);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract Fragment onCreateFragment(Bundle savedInstanceState);
}
