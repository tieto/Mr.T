package com.tieto.systemmanagement.trafficmonitor.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
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
import com.tieto.systemmanagement.trafficmonitor.entity.AppInfoEntity;
import com.tieto.systemmanagement.trafficmonitor.entity.AppNetWorkInfo;
import com.tieto.systemmanagement.trafficmonitor.entity.IptablesForDroidWall;
import com.tieto.systemmanagement.trafficmonitor.storage.AppNetInfoPreferrence;

import java.util.StringTokenizer;

/**
 * Created by jane on 15-4-1.
 */
public class BasicAdapter extends BaseAdapter {
    public Context context;
    private PopupWindow popupWindow;
    public static final String[] pop_list = new String[]{"禁止偷跑","禁止联网","允许网络","仅wifi"};
    private ListView listView;
    private SharedPreferences pref;
    public SharedPreferences.Editor editor;

    /**
     * display the popupWindow
     * @param parent
     * @param appinfo
     */
    public void showWindow(View parent,final AppInfoEntity appinfo) {
        final int uid = appinfo.getUid();
        if(popupWindow == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.t_popup_window,null);
            listView = (ListView) view.findViewById(R.id.pop_list);
            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,pop_list);
            listView.setAdapter(adapter);
            popupWindow = new PopupWindow(view,250,400);

            pref = context.getSharedPreferences(IptablesForDroidWall.PREFS_NAME,Context.MODE_PRIVATE);
            editor = pref.edit();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context,"i:"+i,Toast.LENGTH_SHORT).show();
                String uids_wifi = pref.getString(IptablesForDroidWall.PREF_WIFI_UIDS,"");
                String uids_3g = pref.getString(IptablesForDroidWall.PREF_3G_UIDS,"");
                boolean uid_3g_existed = isUidExisted(uids_3g,uid);
                boolean uid_wifi_existed = isUidExisted(uids_wifi,uid);
                String uids_wifi_new = "";
                String uids_3g_new = "";
                switch (i){
                    case 0://sneak prohibit
                        AppNetInfoPreferrence.saveAppNetState(context,uid,AppNetWorkInfo.SNEAKING_PROHIBIT.ordinal());
                        break;
                    case 1://network prohibit
                        if(uid_wifi_existed) {
                            uids_wifi_new = delete(uids_wifi,uid);
                        }
                        if(uid_3g_existed) {
                            uids_3g_new = delete(uids_3g,uid);
                        }
                        AppNetInfoPreferrence.saveAppNetState(context,uid,AppNetWorkInfo.NETWORK_PROHIBIT.ordinal());
                        break;
                    case 2://network allowed

                        if(!uid_wifi_existed) {
                           uids_wifi_new = save(uids_wifi,uid);
                        } else{
                            uids_wifi_new = uids_wifi;
                        }
                        if(!uid_3g_existed) {
                            uids_3g_new = save(uids_3g,uid);
                        }else{
                            uids_3g_new = uids_3g;
                        }
                        AppNetInfoPreferrence.saveAppNetState(context,uid,AppNetWorkInfo.NETWORK_ALLOWED.ordinal());
                        break;
                    case 3://only wifi allowed
                        if(!uid_wifi_existed){
                            uids_wifi_new = save(uids_wifi,uid);
                        }else{
                            uids_wifi_new = uids_wifi;
                        }
                        uids_3g_new = uids_3g;
                        AppNetInfoPreferrence.saveAppNetState(context,uid,AppNetWorkInfo.WIFI_ALLOWED_ONLY.ordinal());
                        break;
                }
                editor.putString(IptablesForDroidWall.PREF_WIFI_UIDS,uids_wifi_new).commit();
                editor.putString(IptablesForDroidWall.PREF_3G_UIDS,uids_3g_new).commit();
                Log.e("TAG",",uid:"+uid+",uids_wifi_new:"+uids_wifi_new+",uids_3g_new:"+uids_3g_new);

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


    public boolean isUidExisted(String uids, int uid) {
        StringTokenizer tokenizer = new StringTokenizer(uids,"|");
        int totalCount  = tokenizer.countTokens();
        for(int i=0;i<totalCount;i++) {
            int tok = Integer.parseInt(tokenizer.nextToken());
            if(uid == tok) {
                return true;
            }
        }
        return false;
    }

    public String delete(String uids,int uid) {
        StringBuilder builder = new StringBuilder("");
        if(uids != null){
            StringTokenizer tokenizer = new StringTokenizer(uids,"|");
            for(int i=0;i<tokenizer.countTokens();i++) {
                int tok = Integer.parseInt(tokenizer.nextToken());
                if(uid != tok) {
                    if(i!=0) {
                        builder.append("|"+tok);
                    }else{
                        builder.append(tok);
                    }
                }
            }
        }

        return builder.toString();
    }

    public String save(String uids,int uid) {
        return "".equals(uids)?uids+uid:uids+"|"+uid;
    }
}
