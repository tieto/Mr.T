package com.tieto.systemmanagement.authority.model;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.tieto.systemmanagement.TApp;
import com.tieto.systemmanagement.authority.entity.AppPermission;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fliky on 15-4-21.
 */
public class PermissionManager {

    private static final String TAG = "PermissionManager";
    private static PermissionManager INSTANCE;
    private static final String  METHOD_GETOPSFORPACKAGE = "getOpsForPackage";
    private static final Class[] argsTypeForOps = new Class[] {
            int.class
            ,String.class
            ,int[].class
    };
    private Class[] argsTypeForSetMode = new Class[] {
            Integer.TYPE,
            Integer.TYPE,
            String.class,
            Integer.TYPE
    };
    private Class[] argsForGetAllOp = new Class[] {
            int[].class
    };

    private static final Class[] argsTypeForOpToName = new Class[] {
            int.class
    };

    private Method mGetOpsForPackage = null;
    private Method mOpToName = null;
    private AppOpsManager mAppOpsManager = null;
    public ArrayList<Object> appList;
    public String permissionName;
    private int mOp;
    private String mPakcageName;

    public synchronized static final PermissionManager getInstance()
    {
        if(INSTANCE == null)
        {
            try {
                AppOpsManager appOpsManager = (AppOpsManager) TApp.getInstance().getSystemService(Context.APP_OPS_SERVICE);
                Method getOpsForPackage = AppOpsManager.class.getMethod(METHOD_GETOPSFORPACKAGE, argsTypeForOps);
                Method opToName = AppOpsManager.class.getMethod("opToName",argsTypeForOpToName);
                INSTANCE = new PermissionManager(appOpsManager,getOpsForPackage,opToName);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e(TAG, "can't use the AppoOpsManager");
            }
        }
        return INSTANCE;
    }

    private PermissionManager(AppOpsManager appOpsManager ,Method getOpsForPackage,Method opToName)
    {
        mGetOpsForPackage = getOpsForPackage;
        mAppOpsManager = appOpsManager;
        mOpToName = opToName;
    }

    public List<Object> getPerMissionByPacking(PackageInfo packageInfo) {
        int uid = packageInfo.applicationInfo.uid;
        //int uid = TApp.getInstance().getApplicationInfo().uid;
        String packageName = packageInfo.packageName;
        Object[] params = new Object[] {
                uid
                ,packageName
                ,null
        };

        try {
            List packageOps = (List) mGetOpsForPackage.invoke(mAppOpsManager, params);
            if(packageOps != null) {
                Log.i(TAG,packageOps.toString());
                return packageOps;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG,"call getOpsForPackage not success");
        }
        return  null;
    }

    public int getPermissionCountByPackage(List<Object> listPackageOps) {
        int permissionCount = 0;
        if (listPackageOps != null) {
            for (int i = 0; i < listPackageOps.size(); i ++) {
                Object packageOps = listPackageOps.get(i);
                try {
                    Method getOps = packageOps.getClass().getMethod("getOps");
                    List opsEntry = (List) getOps.invoke(packageOps);
                    permissionCount = opsEntry.size();
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.i(TAG,"can't get permission count for:" + packageOps.toString());
                }
            }
        }
        return permissionCount;
    }

    public List<Object> getAllPermissionpackageOps() {
        try {
            Object[] params = new Object[] {
                    null
            };

            Method getOpsForPackage = mAppOpsManager.getClass().getMethod("getPackagesForOps", argsForGetAllOp);
            List packageOps = (List) getOpsForPackage.invoke(mAppOpsManager, params);
            return packageOps;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public ArrayList<AppPermission> getPermissionByPackage(List<Object> listPackageOps,ApplicationInfo appInfo) {
        ArrayList<AppPermission> permissions = new ArrayList<>() ;

        if (listPackageOps != null) {
            for (int i = 0; i < listPackageOps.size(); i ++) {
                Object packageOps = listPackageOps.get(i);
                ArrayList<AppPermission> appPermissions = getAppPermissionByPackage(packageOps,appInfo);
                if(appPermissions != null && appPermissions.size() > 0)
                {
                    permissions.addAll(appPermissions);
                }

            }
        }
        return permissions;
    }

    public ArrayList<AppPermission> getAppPermissionByPackage(Object packageOps,ApplicationInfo appInfo) {
        try {
            ArrayList<AppPermission> permissions = new ArrayList<>() ;
            Method getOps = packageOps.getClass().getMethod("getOps");
            List opsEntries = (List) getOps.invoke(packageOps);
            for(Object OpEntry: opsEntries)
            {
                Method getMode = OpEntry.getClass().getMethod("getMode");
                int mode = (int) getMode.invoke(OpEntry);
                Method getOp = OpEntry.getClass().getMethod("getOp");
                int op = (int) getOp.invoke(OpEntry);
                Object[] params = new Object[] {
                        op
                };
                String name = (String) mOpToName.invoke(mAppOpsManager, params);
                permissions.add(new AppPermission(name, op, mode, appInfo));
            }
            return  permissions;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i(TAG,"can't get permission count for:" + packageOps.toString());
        }
        return  null;
    }

    public boolean setPermission(AppPermission permission,  boolean isChecked) {
        return  setPermissionByOp(permission.op,permission.applicationInfo.uid
                ,permission.applicationInfo.packageName,isChecked);
    }

    public String getAppName(Object packageOps) {
        try {
            Method getPackageName = packageOps.getClass().getMethod("getPackageName");
            String packageName = (String) getPackageName.invoke(packageOps);
            mPakcageName = packageName;
            //TODO:get app name from package name
            return packageName;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return  "";
    }

    public boolean getAppPerMissionIsEnable(Object packageOps) {

        try {
            Method getOps = packageOps.getClass().getMethod("getOps");
            List opsEntries = (List) getOps.invoke(packageOps);
            for (Object OpEntry : opsEntries) {
                Method getMode = OpEntry.getClass().getMethod("getMode");
                int mode = (int) getMode.invoke(OpEntry);
                Method getOp = OpEntry.getClass().getMethod("getOp");
                int op = (int) getOp.invoke(OpEntry);
                Object[] params = new Object[]{
                        op
                };
                String name = (String) mOpToName.invoke(mAppOpsManager, params);
                if(name.equals(permissionName))
                {
                    mOp = op;
                    if(mode == AppOpsManager.MODE_IGNORED)
                    {
                        return  false;
                    }
                    else
                    {
                        return true;
                    }
                }
            }
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setPermissionByObject(Object packageOps, boolean isChecked) {
        try {
            Method getUid = packageOps.getClass().getMethod("getUid");
            int uid = (int) getUid.invoke(packageOps);
            return setPermissionByOp(mOp,uid,mPakcageName,isChecked);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private boolean setPermissionByOp(int op , int uid, String packageName,boolean allow)
    {
        Object[] params = new Object[] {
                op,
                uid,
                packageName,
                allow ? AppOpsManager.MODE_ALLOWED : AppOpsManager.MODE_IGNORED
        };
        try {
            Method setMode = mAppOpsManager.getClass().getMethod("setMode", argsTypeForSetMode);
            setMode.invoke(mAppOpsManager, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
