package com.tieto.systemmanagement.trafficmonitor.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.tieto.systemmanagement.R;

import java.util.ArrayList;

/**
 * Created by jane on 15-4-1.
 */
public class BasicAdapter extends BaseAdapter {
    public Context context;
    private PopupWindow popupWindow;
    public static final String[] pop_list = new String[]{"禁止偷跑","禁止联网","允许网络","仅wifi"};
    private ListView listView;

    public void showWindow(View parent) {
        if(popupWindow == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.t_popup_window,null);
            listView = (ListView) view.findViewById(R.id.pop_list);
            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,pop_list);
            listView.setAdapter(adapter);

            popupWindow = new PopupWindow(view,150,400);
        }
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context,"view"+view+",i:"+i,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth()
                - popupWindow.getWidth() / 2;
        Log.i("coder", "xPos:" + xPos);

        popupWindow.showAsDropDown(parent, xPos, 0);

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
