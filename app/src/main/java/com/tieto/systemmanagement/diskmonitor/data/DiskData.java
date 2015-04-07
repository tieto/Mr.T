package com.tieto.systemmanagement.diskmonitor.data;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.SysManageApplication;
import com.tieto.systemmanagement.diskmonitor.entity.SpaceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangbo on 4/3/15.
 */
public class DiskData {
    private static DiskData ourInstance = new DiskData();

    public static DiskData getInstance() {
        return ourInstance;
    }

    private DiskData() {
    }

    public List<SpaceInfo> getStoreSpaceInfos() {
        ArrayList<SpaceInfo> spaceInfos = new ArrayList<SpaceInfo>();
        
        SpaceInfo info1= new SpaceInfo();
        info1.setIcon(SysManageApplication.getInstance().getDrawable(R.drawable.sysclear_file_apk));
        info1.setTitle(SysManageApplication.getInstance().getString(R.string.disk_space_store_title_1));
        info1.setTotal("123M");
        spaceInfos.add(info1);

        SpaceInfo info2= new SpaceInfo();
        info2.setIcon(SysManageApplication.getInstance().getDrawable(R.drawable.sysclear_file_audio));
        info2.setTitle(SysManageApplication.getInstance().getString(R.string.disk_space_store_title_2));
        info2.setTotal("123M");
        spaceInfos.add(info2);

        SpaceInfo info3= new SpaceInfo();
        info3.setIcon(SysManageApplication.getInstance().getDrawable(R.drawable.sysclear_file_zip));
        info3.setTitle(SysManageApplication.getInstance().getString(R.string.disk_space_store_title_3));
        info3.setTotal("123M");
        spaceInfos.add(info3);

        return spaceInfos;
    }
}
