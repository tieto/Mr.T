package com.tieto.systemmanagement.authority;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tieto.systemmanagement.R;

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
