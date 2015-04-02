package com.tieto.systemmanagement;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by jane on 15-3-26.
 */
public class BasicActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean isNetConnected() {
        return true;
    }
}
