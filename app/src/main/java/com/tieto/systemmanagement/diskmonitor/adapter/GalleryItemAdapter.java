package com.tieto.systemmanagement.diskmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.diskmonitor.entity.ThumbnailInfo;
import com.tieto.systemmanagement.diskmonitor.model.BasicAdapter;
import com.tieto.systemmanagement.diskmonitor.utils.Utils;
import com.tieto.systemmanagement.diskmonitor.views.GalleryItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangbo on 4/20/15.
*/

public class GalleryItemAdapter extends BasicAdapter {
    private LayoutInflater mInflater;
//    private int count;
    private List<Integer> mItemsChecked;
//    public String[] mPaths;
//     String mItemsPath[];
//     Bitmap[] mItems;

    List<ThumbnailInfo> mItems;

    public GalleryItemAdapter(List<ThumbnailInfo> infos) {
//        count = infos.size();
        mItems = infos;
        mItemsChecked = new ArrayList<Integer>();
        for (int i=0; i<mItems.size();i++) {
            mItemsChecked.add(0);
        }

        mInflater = (LayoutInflater) TApp.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mItems.size();
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
        holder.mCheckbox.setChecked(Utils.int2Bool(mItemsChecked.get(position)));

        holder.mCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox)v;
                int id = cb.getId();
                if (Utils.int2Bool(mItemsChecked.get(id))){
                    cb.setChecked(false);
                    mItemsChecked.set(id,0);
                } else {
                    cb.setChecked(true);
                    cb.setVisibility(View.VISIBLE);
                    mItemsChecked.set(id,1);
                }
            }
        });

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                int id = v.getId();
//                ThumbnailInfo info = mItems.get(id);
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse("file://" + info.mItemPath, "image/*");
                //startActivity(intent);
            }
        });

        holder.mImageView.setImageBitmap(mItems.get(position).mItemIcon);
        holder.mCheckbox.setChecked(Utils.int2Bool(mItemsChecked.get(position)));

        holder.mImageID = position;
        return convertView;
    }

    public List<Integer> getItemsChecked() {
        return mItemsChecked;
    }

    public List<String> getItemsPath() {
        List<String> paths = new ArrayList<String>();
        for (int i=0; i< mItems.size();i++) {
            paths.add(mItems.get(i).mItemPath);
        }
        return paths;
    }

    public void deleteItem(String path) {
        String[] paths = new String[mItems.size()];
        for (int i=0; i< mItems.size();i++) {
            if ((mItems.get(i).mItemPath.equalsIgnoreCase(path))) {
                mItems.remove(i);
                mItemsChecked.remove(i);
            }
        }
    }
}