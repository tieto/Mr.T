package com.tieto.systemmanagement.intercept.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.intercept.adapter.CallRecordAdapter;
import com.tieto.systemmanagement.intercept.adapter.MessageRecordAdapter;
import com.tieto.systemmanagement.intercept.entity.Record;
import com.tieto.systemmanagement.intercept.entity.SmsInfo;
import com.tieto.systemmanagement.intercept.recordmanagement.RecordDBHelper;

import java.util.List;

/**
 * Created by zhaooked on 4/10/15.
 */
public class MessageRecordFragment extends ListFragment implements RecordDBHelper.Notification{

    private MessageRecordAdapter mMessageRecordAdapter;

    private List<Record> mRecordList;

    private RecordDBHelper mRecordDBHelper;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_call_record_list,container,false) ;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMessageRecordAdapter = new MessageRecordAdapter(this.getActivity(), mRecordList) ;
        setListAdapter(mMessageRecordAdapter);
        getListView().setEmptyView(getView().findViewById(R.id.prepare_empty_view));

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Save the current click position
                getListView().setTag(position);
            }
        });

        final Button markButton = (Button)getView().findViewById(R.id.button_mark);
        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object position = getListView().getTag();
                if(position != null) {
                    Object selectedItem = getListView().getAdapter().getItem(Integer.valueOf(position.toString()));
                    if (selectedItem instanceof Record) {
                        Record record = (Record) selectedItem;
                        PhoneRecordDialog dialog = new PhoneRecordDialog() ;
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("phone_record", record);
                        dialog.setArguments(bundle);
                        dialog.show(getActivity().getSupportFragmentManager(), "");
                    }
                }else{
                    Toast.makeText(getActivity(),"Please select record first!",Toast.LENGTH_SHORT).show() ;
                }
            }
        });

        final Button deleteButton = (Button) getView().findViewById(R.id.button_delete) ;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordDBHelper.deleteAllMessageRecords() ;
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        mRecordDBHelper = new RecordDBHelper(getActivity()) ;
        mRecordList = mRecordDBHelper.queryMessageInterceptRecord() ;
        mRecordDBHelper.registerNotification(this);
        super.onAttach(activity);
    }

    @Override
    public void notify(Record record) {
        if(record != null) {
            mRecordList.add(record);
        }else{
            mRecordList = mRecordDBHelper.queryMessageInterceptRecord() ;
        }
        mMessageRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        mRecordDBHelper.unRegisterNotification(this);
        super.onDestroy();
    }
}
