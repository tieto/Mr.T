package com.tieto.systemmanagement.diskmonitor.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by wangbo on 4/9/15.
 * http://www.androidhub4you.com/2012/09/code-for-audio-player-in-android.html#ixzz3Triz2VNq
 */
public class Mp3Filter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3"));
    }
}