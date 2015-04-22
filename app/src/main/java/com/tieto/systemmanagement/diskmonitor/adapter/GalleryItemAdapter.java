package com.tieto.systemmanagement.diskmonitor.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.diskmonitor.entity.ThumbNailInfo;
import com.tieto.systemmanagement.diskmonitor.views.GalleryItemViewHolder;

/**
 * Created by wangbo on 4/20/15.
*/

public class GalleryItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private int count;
    private boolean[] mItemsChecked;
    public String[] mPaths;
     String mItemsPath[];
     Bitmap[] mItems;

    public GalleryItemAdapter(ThumbNailInfo info) {
        mPaths = info.mArrPath;
        mItemsPath = info.mItemlPath;
        mItems = info.mItem;
        count = mItemsPath.length;
        mItemsChecked = new boolean[count];

        mInflater = (LayoutInflater) TApp.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return count;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        GalleryItemViewHolder holder;
        if (convertView == null) {
            holder = new GalleryItemViewHolder();
            convertView = mInflater.inflate(
                    R.layout.item_disk_gallery, null);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            holder.mCheckbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

            convertView.setTag(holder);
        }
        else {
            holder = (GalleryItemViewHolder) convertView.getTag();
        }

        holder.mCheckbox.setId(position);
        holder.mImageView.setId(position);

        holder.mCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox)v;
                int id = cb.getId();
                if (mItemsChecked[id]){
                    cb.setChecked(false);
                    mItemsChecked[id] = false;
                } else {
                    cb.setChecked(true);
                    cb.setVisibility(View.VISIBLE);
                    mItemsChecked[id] = true;
                }
            }
        });

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + mPaths[id]), "image/*");
                //startActivity(intent);
            }
        });

        holder.mImageView.setImageBitmap(mItems[position]);
        holder.mCheckbox.setChecked(mItemsChecked[position]);

        holder.mImageID = position;
        return convertView;
    }

    public boolean[] getItemsChecked() {
        return mItemsChecked;
    }
}