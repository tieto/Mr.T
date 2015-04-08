package com.tieto.systemmanagement;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

/**
 * Created by jane on 15-3-26.
 */
public class BasicActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String getReadableString(long byteSize) {
        if (byteSize < 1024) {
            return String.format("%dB/s", byteSize);
        } else if (byteSize < 1024 * 1024) {
            return String.format("%dKB/s", Math.round(byteSize / 1024.0f));
        } else {
            return String.format("%.1fMB/s", byteSize / (float)(1024 * 1024));
        }
    }
}
