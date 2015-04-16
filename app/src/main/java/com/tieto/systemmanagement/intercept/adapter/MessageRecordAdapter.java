package com.tieto.systemmanagement.intercept.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.intercept.entity.Record;
import com.tieto.systemmanagement.intercept.entity.SmsInfo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by zhaooked on 4/10/15.
 */
public class MessageRecordAdapter extends BaseAdapter {

    private List<Record> records;
    private LayoutInflater layoutInflater ;
    private Context context ;

    public MessageRecordAdapter(Context context, List<Record> record) {
        this.context = context ;
        this.records = record ;
        layoutInflater = LayoutInflater.from(context) ;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_message_record_list,parent,false) ;
            viewHolder = new ViewHolder();
            viewHolder.recordNumberView = (TextView)convertView.findViewById(R.id.record_number) ;
            viewHolder.recordView = (TextView)convertView.findViewById(R.id.content) ;
            viewHolder.dateView = (TextView)convertView.findViewById(R.id.date);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag() ;
        }
        bindView(viewHolder, position);

        return convertView ;
    }

    private void bindView(ViewHolder viewHolder , int position){
        viewHolder.dateView.setText(records.get(position).getDate());
        viewHolder.recordView.setText(records.get(position).getRecordContent());
        viewHolder.recordNumberView.setText(records.get(position).getReMark());
    }

    static class ViewHolder {
        public TextView recordNumberView ;
        public TextView recordView ;
        public TextView dateView ;
    }
}
