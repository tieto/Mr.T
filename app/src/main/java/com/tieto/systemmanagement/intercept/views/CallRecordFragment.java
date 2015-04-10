package com.tieto.systemmanagement.intercept.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.intercept.adapter.CallRecordAdapter;
import com.tieto.systemmanagement.intercept.entity.Record;
import com.tieto.systemmanagement.intercept.recordmanagement.RecordDBHelper;

import java.util.List;

/**
 * Created by zhaooked on 4/10/15.
 */
public class CallRecordFragment extends Fragment implements RecordDBHelper.Notification{

    private CallRecordAdapter callRecordAdapter ;

    private List<Record> recordList ;

    private RecordDBHelper recordDBHelper ;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_intercept_pager,container,false) ;

        ((View)rootView.findViewById(R.id.fragment_content)).setVisibility(View.VISIBLE);

        ListView callRecordListView = ((ListView)rootView.findViewById(R.id.intercept_configuration)) ;
        callRecordListView.setVisibility(View.VISIBLE);
        callRecordAdapter = new CallRecordAdapter(this.getActivity(),recordList) ;
        callRecordListView.setAdapter(callRecordAdapter);

        return rootView ;
    }

    @Override
    public void onAttach(Activity activity) {
        recordDBHelper = new RecordDBHelper(getActivity()) ;
        recordList = recordDBHelper.queryPhoneInterceptRecord() ;
        super.onAttach(activity);
    }

    @Override
    public void notify(Record record) {
        recordList.add(record) ;
        callRecordAdapter.notifyDataSetChanged();
    }
}
