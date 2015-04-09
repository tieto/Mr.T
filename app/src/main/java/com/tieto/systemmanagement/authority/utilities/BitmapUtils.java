package com.tieto.systemmanagement.authority.utilities;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * @author Jiang Ping
 */
public final class BitmapUtils {
    private BitmapUtils() {
        throw new UnsupportedOperationException("Utility class do not allow new instance");
    }

    public static Bitmap convertDrawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap createReflectBitmap(Bitmap bitmap, int refHeight){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Create upside down bitmap
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap refBitmap = Bitmap.createBitmap(bitmap, 0, height - refHeight,
                width, refHeight, matrix, false);

        // Target bitmap to draw
        Bitmap targetBitmap = Bitmap.createBitmap(width, height +refHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(refBitmap, 0, height + 1, null);

        // Create gradient effect
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(),
                0, targetBitmap.getHeight() + 1,
                0x90ffffff, 0x00ffffff, Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, height + 1, width, targetBitmap.getHeight(), paint);

        // Cleanup the memory
        refBitmap.recycle();
        return targetBitmap;
    }
}
