package com.tieto.systemmanagement.authority.entity;

import android.graphics.Bitmap;

/**
 * @author Jiang Ping
 */
public final class BitmapUtils {
    private BitmapUtils() {
        throw new UnsupportedOperationException("Utility class do not allow new instance");
    }

    public static Bitmap createReflectBitmap(Bitmap bitmap, int refHeight){
        return bitmap;
    }
}
