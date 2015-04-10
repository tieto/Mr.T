package com.tieto.systemmanagement.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tieto.systemmanagement.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jinpei on 09/04/15.
 */
public class AutoCalculateTextView extends TextView{

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_SIZE = 1;
    private static final int TYPE_DATE = 2;

    private static final String[] UNITS = {"B","KB","MB","GB","TB"};
    private static final int MODULE = 1024;
    private static final String MIN_SIZE = "0";

    private int contentType;

    public AutoCalculateTextView(Context context) {
        super(context);
    }

    public AutoCalculateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoCalculateTextView);
        this.contentType = typedArray.getInt(R.styleable.AutoCalculateTextView_setContentType, TYPE_TEXT);
        typedArray.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        switch (contentType) {
            case TYPE_TEXT:
                break;
            case TYPE_SIZE:
                text = formatToSize(text);
                break;
            case TYPE_DATE:
                text = formatToDate(text);
                break;
        }
        super.setText(text, type);
    }

    public static CharSequence formatToSize(CharSequence size_old) {
        String str = size_old.toString();
        if(str == null || "".equals(str)) return MIN_SIZE;

        StringBuffer result = new StringBuffer();
        Double size = Double.valueOf(str);

        if (size < 0) return MIN_SIZE;

        int unit = 0;
        while(size > MODULE && unit <= UNITS.length) {
            size /= MODULE;
            unit++;
        }
        result.append(String.format("%.2f", size + 0.005));
        result.append(UNITS[unit]);
        return result;
    }

    public static CharSequence formatToDate(CharSequence date_old) {
        if(date_old == null || "".equals(date_old.toString())) return "";

        Long date_long = Long.valueOf(date_old.toString());
        Date date = new Date(date_long);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        return format.format(date);
    }
}