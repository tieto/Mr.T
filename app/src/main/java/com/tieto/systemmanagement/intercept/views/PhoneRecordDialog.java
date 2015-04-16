package com.tieto.systemmanagement.intercept.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.intercept.entity.Record;
import com.tieto.systemmanagement.intercept.recordmanagement.RecordDBHelper;

/**
 * Created by zhaooked on 4/14/15.
 */
public class PhoneRecordDialog extends AbsDialogFragment {

    private Record record ;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_dialog_call_record_mark, container, false);

        Bundle bundle = getArguments() ;
        record = bundle.getParcelable("phone_record") ;
        //Default selected.
        final RadioButton whiteRadio = (RadioButton) rootView.findViewById(R.id.radio_white);
        whiteRadio.setChecked(true);

        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        rootView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.setManifestType(whiteRadio.isChecked() ? Record.ManifestType.WHITE_LIST : Record.ManifestType.BLACK_LIST);
                record.setInterceptType(Record.InterceptType.INCOMING_PHONE);
                new RecordDBHelper(getActivity()).updateRecord(record);
                getDialog().cancel();
            }
        });

        getDialog().setTitle(record.getRecordContent());
        return rootView;
    }

    
}
