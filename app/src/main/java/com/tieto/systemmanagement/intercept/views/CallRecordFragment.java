package com.tieto.systemmanagement.intercept.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.tieto.systemmanagement.intercept.entity.Record;
import com.tieto.systemmanagement.intercept.recordmanagement.RecordDBHelper;

import java.util.List;

/**
 * Created by zhaooked on 4/10/15.
 */
public class CallRecordFragment extends ListFragment {

    private CallRecordAdapter mCallRecordAdapter;

    private List<Record> mRecordList;

    private RecordDBHelper mRecordDBHelper;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        mCallRecordAdapter = new CallRecordAdapter(this.getActivity(), mRecordList) ;

        return inflater.inflate(R.layout.activity_call_record_list,container,false) ;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setListAdapter(mCallRecordAdapter);
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
                if(!mRecordList.isEmpty()) {

                    new AsyncTask<String,Integer,String>(){

                        private ProgressDialog progressDialog ;

                        @Override
                        protected String doInBackground(String[] params) {
                            mRecordDBHelper.deleteAllPhoneRecords();
                            return null;
                        }

                        @Override
                        protected void onPreExecute() {
                            progressDialog = new ProgressDialog(getActivity()).show(getActivity(), null, "Deleting ...", true);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            progressDialog.dismiss();

                        }
                    }.execute(null,null,null) ;
                }
            }
        });
    }

    private void updateUI(Record record) {
        if(record == null ) {
            mRecordList.clear();
            mRecordList.addAll(mRecordDBHelper.queryPhoneInterceptRecord());
        }else if(record.getInterceptType() == Record.InterceptType.INCOMING_PHONE){
            mRecordList.add(record);
        }
        mCallRecordAdapter.notifyDataSetChanged();
    }

    private NotifyUIInterface notifyListener ;

    @Override
    public void onAttach(Activity activity) {

        notifyListener = new NotifyUIInterface(getActivity()) {
            @Override
            public void doNotify(Record record) {
                updateUI(record);
            }
        } ;

        mRecordDBHelper = new RecordDBHelper(getActivity()) ;
        mRecordList = mRecordDBHelper.queryPhoneInterceptRecord() ;

        mRecordDBHelper.registerNotification(notifyListener);
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        mRecordDBHelper.unRegisterNotification(notifyListener);
        super.onDestroy();
    }
}
