package com.tieto.systemmanagement.authority.entity;

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
import android.support.annotation.Nullable;

/**
 * @author Jiang Ping
 */
public final class BitmapWorker {

    /** Real bitmap to set */
    private Bitmap mBitmap;

    /** Init the bitmap with source bitmap, the bitmap handle will be kept */
    public BitmapWorker(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    /** Init the bitmap with drawable */
    public BitmapWorker(Drawable drawable) {
        mBitmap = convertFromDrawable(drawable);
    }

    @Nullable
    public Bitmap getBitmap() {
        return mBitmap;
    }

    private Bitmap convertFromDrawable(Drawable drawable) {
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

    /** Every call will create new instance */
    public Bitmap createReflectBitmap(float refRatio){
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        int refHeight = Math.round(refRatio * height);

        // Create upside down bitmap
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap refBitmap = Bitmap.createBitmap(mBitmap, 0, height - refHeight,
                width, refHeight, matrix, false);

        // Target bitmap to draw
        Bitmap targetBitmap = Bitmap.createBitmap(width, height +refHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawBitmap(refBitmap, 0, height + 1, null);

        // Create gradient effect
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, mBitmap.getHeight(),
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
