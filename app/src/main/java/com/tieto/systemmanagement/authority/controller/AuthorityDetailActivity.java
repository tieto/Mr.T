package com.tieto.systemmanagement.authority.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class AuthorityDetailActivity extends AbsFragmentActivity {

    @Override
    protected Fragment onCreateFragment(Bundle savedInstanceState) {
        if (getActionBar() != null) {
            getActionBar().hide();
        }

        Fragment fragment = AuthorityDetailFragment.newInstance();
        fragment.setArguments(getIntent().getExtras());
        return fragment;
    }
}
