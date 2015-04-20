package com.tieto.systemmanagement.diskmonitor.views;

import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by wangbo on 4/20/15.
 * For performance purpose: holds child views for each  item .
 */

public class AudioItemViewHolder {
    private CheckBox mCheckBox;
    private TextView mTextView;

    public AudioItemViewHolder() {
    }

    public AudioItemViewHolder(TextView textView, CheckBox checkBox) {
        this.mCheckBox = checkBox;
        this.mTextView = textView;
    }

    public CheckBox getCheckBox() {
        return mCheckBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.mCheckBox = checkBox;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setTextView(TextView textView) {
        this.mTextView = textView;
    }
}