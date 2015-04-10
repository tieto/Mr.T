package com.tieto.systemmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gujiao on 24/03/15.
 */
public class SystemManagementAdapter extends BaseAdapter {

    //TODO:the class member var need start m
    private Context context;
    private LayoutInflater inflater;
    private List<FunEntity> data; //TODO: change the var name

    public SystemManagementAdapter(Context context, List<FunEntity> data) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return this.data == null ? 0 : this.data.size();
    }

    @Override
    public FunEntity getItem(int position) {
        return this.data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_main_grid, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.image.setImageResource(data.get(position).getIconRes());
        viewHolder.text.setText(data.get(position).getTitleRes());
        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
        TextView text;
    }
}
