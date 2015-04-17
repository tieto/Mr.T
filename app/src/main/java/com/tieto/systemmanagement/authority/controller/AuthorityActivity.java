package com.tieto.systemmanagement.authority.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class AuthorityActivity extends AbsFragmentActivity {

    @Override
    protected Fragment onCreateFragment(Bundle savedInstanceState) {
        return MainPagerFragment.newInstance();
    }
}
