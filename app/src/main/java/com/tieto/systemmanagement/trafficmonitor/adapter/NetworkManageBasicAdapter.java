package com.tieto.systemmanagement.trafficmonitor.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
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
import com.tieto.systemmanagement.authority.entity.AppWrapper;
import com.tieto.systemmanagement.trafficmonitor.entity.FirewallType;
import com.tieto.systemmanagement.trafficmonitor.entity.IptablesForDroidWall;
import com.tieto.systemmanagement.trafficmonitor.storage.TrafficMonitorPref;

import java.util.StringTokenizer;

/**
 * Created by jane on 15-4-1.
 */
public class NetworkManageBasicAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    public Context context;
    private PopupWindow popupWindow;
    private ListView mListView;
    private SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;
    private int mUid;
    private CallBack mCallback;

    /**
     * display the popupWindow when the button was clicked
     * @param parent the view which is clicked
     * @param appWrapper
     * @param callBack to change the info when the choice is changed
     */
    public void showWindow(final View parent,final View arrow,final AppWrapper appWrapper,
                           final CallBack callBack) {
        //parent should be changed by animation
        int h = arrow.getLayoutParams().height;
        int w = arrow.getLayoutParams().width;

        final Animation anim1 = new RotateAnimation(0f,180f,w/2,h/2);
        anim1.setRepeatCount(0);
        anim1.setDuration(50);
        anim1.setFillAfter(true);

        final Animation anim2 = new RotateAnimation(180f,0f,w/2,h/2);
        anim2.setRepeatCount(0);
        anim2.setDuration(50);
        anim2.setFillAfter(true);


        mUid = appWrapper.getUid();
        mCallback = callBack;
        if(popupWindow == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.t_popup_window,null);
            mListView = (ListView) view.findViewById(R.id.pop_list);
            ArrayAdapter adapter = new ArrayAdapter(
                    context, android.R.layout.simple_list_item_1, FirewallType.FIREWALL_TYPE_NAME);
            mListView.setAdapter(adapter);
            popupWindow = new PopupWindow(view,250,400);

            mSharedPreferences = context.getSharedPreferences(
                    IptablesForDroidWall.PREFS_NAME,Context.MODE_PRIVATE);
            mEditor = mSharedPreferences.edit();
        }
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth()
                - popupWindow.getWidth();
        popupWindow.showAsDropDown(parent, xPos, 0);

        arrow.startAnimation(anim1);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                arrow.startAnimation(anim2);
            }
        });
        mListView.setOnItemClickListener(this);
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


     // check if the app  is already existed
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


     // delete the specified app
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

    //add the specified uidApp
    public String save(String uids,int uid) {
        return "".equals(uids)?uids+uid:uids+"|"+uid;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String uid_wifi = mSharedPreferences.getString(
                IptablesForDroidWall.PREF_WIFI_UIDS, "");
        String uid_3g = mSharedPreferences.getString(
                IptablesForDroidWall.PREF_3G_UIDS, "");
        boolean uid_3g_existed = isUidExisted(uid_3g, mUid);
        boolean uid_wifi_existed = isUidExisted(uid_wifi, mUid);
        String uid_wifi_new = "";
        String uid_3g_new = "";
        switch (position) {
            case 0://sneak prohibit
                mCallback.onNetAllowedInfoUpdated(FirewallType.FIREWALL_TYPE_NAME[0]);
                TrafficMonitorPref.save(context,0,
                        TrafficMonitorPref.FLAG_FIREWALL_TYPE, mUid);
                break;
            case 1://network prohibit
                mCallback.onNetAllowedInfoUpdated(FirewallType.FIREWALL_TYPE_NAME[1]);
                if (uid_wifi_existed) {
                    uid_wifi_new = delete(uid_wifi, mUid);
                }
                if (uid_3g_existed) {
                    uid_3g_new = delete(uid_3g, mUid);
                }
                TrafficMonitorPref.save(context, 1,
                        TrafficMonitorPref.FLAG_FIREWALL_TYPE, mUid);
                break;
            case 2://network allowed
                mCallback.onNetAllowedInfoUpdated(FirewallType.FIREWALL_TYPE_NAME[2]);
                if (!uid_wifi_existed) {
                    uid_wifi_new = save(uid_wifi, mUid);
                } else {
                    uid_wifi_new = uid_wifi;
                }
                if (!uid_3g_existed) {
                    uid_3g_new = save(uid_3g, mUid);
                } else {
                    uid_3g_new = uid_3g;
                }
                TrafficMonitorPref.save(context, 2,
                        TrafficMonitorPref.FLAG_FIREWALL_TYPE, mUid);
                break;
            case 3://only wifi allowed
                mCallback.onNetAllowedInfoUpdated(FirewallType.FIREWALL_TYPE_NAME[3]);
                if (!uid_wifi_existed) {
                    uid_wifi_new = save(uid_wifi, mUid);
                } else {
                    uid_wifi_new = uid_wifi;
                }
                uid_3g_new = uid_3g;
                TrafficMonitorPref.save(context, 3,
                        TrafficMonitorPref.FLAG_FIREWALL_TYPE, mUid);
                break;
        }

        //save uids
        mEditor.putString(IptablesForDroidWall.PREF_WIFI_UIDS, uid_wifi_new).commit();
        mEditor.putString(IptablesForDroidWall.PREF_3G_UIDS, uid_3g_new).commit();
        //dismiss the popup window
        popupWindow.dismiss();
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
