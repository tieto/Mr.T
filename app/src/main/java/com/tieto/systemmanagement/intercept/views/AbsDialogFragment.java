package com.tieto.systemmanagement.intercept.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tieto.systemmanagement.intercept.entity.Record;

/**
 * Created by zhaooked on 4/14/15.
 */
public abstract class AbsDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return createView(inflater,container,savedInstanceState) ;
    }

    public abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) ;
}
