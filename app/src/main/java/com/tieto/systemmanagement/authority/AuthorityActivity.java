package com.tieto.systemmanagement.authority;

import com.tieto.systemmanagement.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class AuthorityActivity extends AbsFragmentActivity {

    @Override
    protected Fragment onCreateFragment(Bundle savedInstanceState) {
        return AuthorityAppListFragment.newInstance();
    }
}
