package com.tieto.systemmanagement.trafficmonitor.utils;

/**
 * Created by jane on 15-4-18.
 */
public class CommonMethod {
    public static String formatString(long byteSize, boolean isSpeed) {
        if (byteSize < 1024) {
            return isSpeed ? String.format("%dB/s", byteSize):String.format("%dB", byteSize);
        } else if (byteSize < 1024 * 1024) {
            return isSpeed ? String.format("%dKB/s", Math.round(byteSize / 1024.0f))
                    : String.format("%dKB", Math.round(byteSize / 1024.0f));
        } else {
            return isSpeed ?String.format("%.1fMB/s", byteSize / (float)(1024 * 1024))
                    :String.format("%.1fMB", byteSize / (float)(1024 * 1024));
        }
    }
}
