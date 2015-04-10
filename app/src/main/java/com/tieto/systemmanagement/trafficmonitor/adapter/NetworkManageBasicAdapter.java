package com.tieto.systemmanagement.trafficmonitor.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.trafficmonitor.entity.AppInfoEntity;
import com.tieto.systemmanagement.trafficmonitor.entity.FirewallType;
import com.tieto.systemmanagement.trafficmonitor.entity.IptablesForDroidWall;
import com.tieto.systemmanagement.trafficmonitor.storage.FirewallTypePreferrence;

import java.util.StringTokenizer;

/**
 * Created by jane on 15-4-1.
 */
public class NetworkManageBasicAdapter extends BaseAdapter {
    public Context context;
    private PopupWindow popupWindow;
    public static final String[] mPop_list = new String[]{"禁止偷跑","禁止联网","允许网络","仅wifi"};
    private ListView mListView;
    private SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;

    /**
     * display the popupWindow when the button was clicked
     * @param parent the view which is clicked
     * @param appinfo
     * @param callBack to change the info when the choice is changed
     */
    public void showWindow(final View parent,final AppInfoEntity appinfo, final CallBack callBack) {
        //parent should be changed by animation
        int h = parent.getLayoutParams().height;
        int w = parent.getLayoutParams().width;

        final Animation anim1 = new RotateAnimation(0f,180f,w/2,h/2);
        anim1.setRepeatCount(0);
        anim1.setDuration(50);
        anim1.setFillAfter(true);

        final Animation anim2 = new RotateAnimation(180f,0f,w/2,h/2);
        anim2.setRepeatCount(0);
        anim2.setDuration(50);
        anim2.setFillAfter(true);


        final int uid = appinfo.getUid();
        if(popupWindow == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.t_popup_window,null);
            mListView = (ListView) view.findViewById(R.id.pop_list);
            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, mPop_list);
            mListView.setAdapter(adapter);
            popupWindow = new PopupWindow(view,250,400);

            mSharedPreferences = context.getSharedPreferences(IptablesForDroidWall.PREFS_NAME,Context.MODE_PRIVATE);
            mEditor = mSharedPreferences.edit();
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String uids_wifi = mSharedPreferences.getString(IptablesForDroidWall.PREF_WIFI_UIDS, "");
                String uids_3g = mSharedPreferences.getString(IptablesForDroidWall.PREF_3G_UIDS, "");
                boolean uid_3g_existed = isUidExisted(uids_3g, uid);
                boolean uid_wifi_existed = isUidExisted(uids_wifi, uid);
                String uids_wifi_new = "";
                String uids_3g_new = "";
                switch (i) {
                    case 0://sneak prohibit
                        callBack.onNetAllowedInfoUpdated(mPop_list[0]);
                        FirewallTypePreferrence.saveNetworkState(context, uid, FirewallType.SNEAKING_PROHIBIT.ordinal());
                        break;
                    case 1://network prohibit
                        callBack.onNetAllowedInfoUpdated(mPop_list[1]);
                        if (uid_wifi_existed) {
                            uids_wifi_new = delete(uids_wifi, uid);
                        }
                        if (uid_3g_existed) {
                            uids_3g_new = delete(uids_3g, uid);
                        }
                        FirewallTypePreferrence.saveNetworkState(context, uid, FirewallType.NETWORK_PROHIBIT.ordinal());
                        break;
                    case 2://network allowed
                        callBack.onNetAllowedInfoUpdated(mPop_list[2]);
                        if (!uid_wifi_existed) {
                            uids_wifi_new = save(uids_wifi, uid);
                        } else {
                            uids_wifi_new = uids_wifi;
                        }
                        if (!uid_3g_existed) {
                            uids_3g_new = save(uids_3g, uid);
                        } else {
                            uids_3g_new = uids_3g;
                        }
                        FirewallTypePreferrence.saveNetworkState(context, uid, FirewallType.NETWORK_ALLOWED.ordinal());
                        break;
                    case 3://only wifi allowed
                        callBack.onNetAllowedInfoUpdated(mPop_list[3]);
                        if (!uid_wifi_existed) {
                            uids_wifi_new = save(uids_wifi, uid);
                        } else {
                            uids_wifi_new = uids_wifi;
                        }
                        uids_3g_new = uids_3g;
                        FirewallTypePreferrence.saveNetworkState(context, uid, FirewallType.WIFI_ALLOWED_ONLY.ordinal());
                        break;
                }
                parent.startAnimation(anim2);
                //save uids
                mEditor.putString(IptablesForDroidWall.PREF_WIFI_UIDS, uids_wifi_new).commit();
                mEditor.putString(IptablesForDroidWall.PREF_3G_UIDS, uids_3g_new).commit();
                //dismiss the popup window
                popupWindow.dismiss();
                Log.e("TAG", ",uid:" + uid + ",uids_wifi_new:" + uids_wifi_new + ",uids_3g_new:" + uids_3g_new);

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

        parent.startAnimation(anim1);


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

    /**
     * check if the uid is already existed
     * @param uids
     * @param uid the specified uid
     * @return
     */
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

    /**
     * delete the specified uid
     * @param uids
     * @param uid
     * @return
     */
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

    /**
     * add the specified uid
     * @param uids
     * @param uid
     * @return
     */
    public String save(String uids,int uid) {
        return "".equals(uids)?uids+uid:uids+"|"+uid;
    }

    public interface CallBack{
        void onNetAllowedInfoUpdated(String info);
    }

    //the callback was designed to update the ui display
    //when the firewall type changed.
    public class CallbackImpl implements CallBack{
        private TextView mNetAllowedInfo;
        public CallbackImpl(TextView mText){
            mNetAllowedInfo = mText;
        }

        @Override
        public void onNetAllowedInfoUpdated(String info) {
            mNetAllowedInfo.setText(info);
        }
    }
}
