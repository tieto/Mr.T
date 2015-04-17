package com.tieto.systemmanagement.authority.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jiang Ping
 */
public class PermissionListFragment extends Fragment {

    private static List<PermissionItem> PERMISSION_LIST = new ArrayList<PermissionItem>();
    static {
        PERMISSION_LIST.add(new PermissionItem("Call", "%d apps have permission to make call"));
        PERMISSION_LIST.add(new PermissionItem("SMS", "%d apps have permission to read/write SMS"));
        PERMISSION_LIST.add(new PermissionItem("Contacts", "%d apps have permission to read/write contacts"));
        PERMISSION_LIST.add(new PermissionItem("Call log", "%d apps have permission to read/write call logs"));
        PERMISSION_LIST.add(new PermissionItem("Location", "%d apps have permission to read your location"));
    }

    public static PermissionListFragment newInstance() {
        return new PermissionListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authority_permission_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list = (ListView) getView().findViewById(android.R.id.list);
        list.setAdapter(new Adapter(getActivity()));
    }

    private static class Adapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public Adapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return PERMISSION_LIST.size();
        }

        @Override
        public PermissionItem getItem(int i) {
            return PERMISSION_LIST.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = mInflater.inflate(R.layout.authority_permission_list_item, viewGroup, false);
                holder = new ViewHolder();
                holder.title = (TextView) view.findViewById(R.id.title);
                holder.description = (TextView) view.findViewById(R.id.description);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            PermissionItem item = PERMISSION_LIST.get(i);
            holder.title.setText(item.getTitle());
            holder.description.setText(String.format(item.getDescription(), i));
            return view;
        }
    }

    private final static class ViewHolder {
        TextView title;
        TextView description;
    }

    private final static class PermissionItem {
        private String mTitle;
        private String mDescription;

        public PermissionItem(String title, String desc) {
            mTitle = title;
            mDescription = desc;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getDescription() {
            return mDescription;
        }
    }
}
