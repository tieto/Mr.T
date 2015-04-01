package com.tieto.systemmanagement.trafficmonitor.entity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.*;
import android.util.Log;
//import android.os.Process;

import com.tieto.systemmanagement.trafficmonitor.TrafficActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.Process;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by jane on 15-3-28.
 */
public final class Api {
    public static final String VERSION = "1.3.6";
    //Preferrences
    public static final String PREFS_NAME = "DroidWallPrefs";
    public static final String PREF_ALLOWEDUIDS = "AllowedUids";
    public static final String PREF_PASSWORD = "Password";
    public static final String PREF_MODE = "BlockMode";
    public static final String PREF_ITFS = "Interfaces";


    //Modes
    public static final String MODE_WHITELIST = "whitelist";
    public static final String MODE_BLACKLIST = "blacklist";

    //Interfaces
    public static final String ITF_3G = "3G/2G";
    public static final String ITF_WIFI = "wi-fi";

    //cached applications
    public static DroidApp applications[] = null;
    //do we have "wireless tether for Root Users" installed?
    public static String hastether = null;
    //do we have root access?
    private static boolean hasroot = false;

    private static Context mContext;

    private static final String SCRIPT_FILE = "droidwall.sh";

    /**
     * display a simple alert box
     * @param context
     * @param msg
     */
    public static void alert(Context context,CharSequence msg) {
        if(context != null) {
            new AlertDialog.Builder(context)
                    .setNeutralButton("OK",null)
                    .setMessage(msg)
                    .show();
        }
    }

    private static boolean applyIptablesRulesImpl(Context context,List<Integer> uids, boolean showErrors) {
        if(context == null) {
            return false;
        }
        mContext = context;
        final SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        final boolean whitelist = prefs.getString(PREF_MODE,MODE_WHITELIST).equals(MODE_WHITELIST);
        boolean wifi = false;//wifi selected?
        final String itfs = prefs.getString(PREF_ITFS,ITF_3G);
        String itfFilter;
        if(itfs.indexOf("|")!=-1) {
            itfFilter = "";;
            wifi = true;
        } else if(itfs.indexOf(ITF_3G) != -1) {
            itfFilter = " -o rmnet+ ";;
        } else {
            itfFilter = " -o tiwlan ";;
            wifi = true;
        }

        final StringBuilder script = new StringBuilder();
        try{
            int code;
            script.append("iptables -F || exit/n");
            final String targetRule = (whitelist?"REJECT":"REJECT");
            if(whitelist && wifi) {
                int uid = android.os.Process.getUidForName("dhcp");//???dhcp
                if(uid != -1) {
                    script.append("iptables -A OUTPUT" + itfFilter +"-m owner --uid-owner "
                    +uid+" -j ACCEPT || exit/n");
                }
                uid = android.os.Process.getUidForName("wifi");
                if(uid != -1) {
                    script.append("iptables -A OUTPUT " + itfFilter + "-m owner --uid-owner "+uid+" -j ACCEPT || exit/n");
                }

            }
            for (Integer uid : uids) {
                script.append("iptables -A OUTPUT" + itfFilter + "-m owner --uid-owner " +uid+" -j "+targetRule+" ||exit/n");
            }
            if(whitelist){
                script.append("iptables -A OUTPUT"+itfFilter+"-j REJECT || exit/n");
            }
            StringBuilder res = new StringBuilder();
            code = runScriptAsRoot(script.toString(),res);
            if(showErrors && code !=0){
                String  msg = res.toString();
                if(msg.indexOf("Couldn't find match `owner`")!=-1 ||msg.indexOf("no chain/target match ")!=-1) {
                    alert(context,"Error applying iptables rules ./n Exit code:"+code +"/n/n"+
                    "It seems your Linux kernel was not compiled with the netFilter /\"owner/\"module enabled, which is required for Droid Wall to work properly./n/n\" + " +
                            "You should check if an updated version of you android ROM compiled with this kernel module.");
                } else {
                    if(msg.indexOf("/nTry `iptables -h` or `iptables --help ` for more information.")!=-1) {
                        msg = msg.replace("/nTry `iptables -h ` or `iptables --help` for more information.","");
                    }
                    alert(context,"Error applying iptables rules.Eixt code:"+code+"/n/n"+msg.trim());
                }

            } else {
                return true;
            }
        }catch (Exception e) {
            if(showErrors) {
                alert(context,"error refreshing iptables:"+e);
            }
        }
        return false;
    }
    public static boolean applySavedIptablesRules(Context context,boolean showErrors) {
        if(context == null) {
           return false;
        }
        //for test
//        String test = "com.android.chrome|com.tencent.mobileqq";
        StringBuilder test = new StringBuilder("");


        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()){
            if(test.toString() != ""){
//                test.append("|");
            }
                if("com.android.chrome".equals(appProcess.processName)){
                    test.append( appProcess.uid);
                    Log.e("TAG",appProcess.processName+":"+appProcess.uid);
                }



        }
        final String savedNames = context.getSharedPreferences(PREFS_NAME,0).getString(PREF_ALLOWEDUIDS,test.toString());
        List<Integer> uids = new LinkedList<Integer>();
//        PackageManager pm = mContext.getPackageManager();
        if(savedNames.length()>0) {
            final StringTokenizer tok = new StringTokenizer(savedNames,"|");
            while (tok.hasMoreTokens()) {
                String temp = tok.nextToken();
                try {
//                    ApplicationInfo appinfo = pm.getApplicationInfo(temp,1);
                    uids.add(Integer.parseInt(temp));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        return applyIptablesRulesImpl(context,uids,showErrors);
    }

    public static boolean applyIptablesRules(Context context,boolean showErrors) {
        if(context == null) {
            return false;
        }
        final SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,0);
        List<Integer> uidsToApply = new LinkedList<Integer>();
        final DroidApp[] apps = getApps(context);

        final StringBuilder newNames = new StringBuilder();
        for(int i=0;i<apps.length;i++) {
            if(apps[i].selected) {
                if(newNames.length() != 0){
                    newNames.append('|');
                }
                newNames.append(apps[i].username);
                uidsToApply.add(apps[i].uid);
            }
        }
        if(!newNames.toString().equals(prefs.getString(PREF_ALLOWEDUIDS,""))) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PREF_ALLOWEDUIDS,newNames.toString());
            editor.commit();
        }
        return applyIptablesRulesImpl(context,uidsToApply,showErrors);
    }

    public static boolean purgeIptables(Context context) {
        StringBuilder res = new StringBuilder();
        try {
            int code  = runScriptAsRoot("iptables -F || exit/n",res);
            if(code != 0) {
                alert(context,"error purging iptables .exit code :"+code+"/n"+res);
                return false;
            }
            return true;
        } catch (Exception e) {
            alert(context,"error purging iptables:"+e);
            return false;
        }
    }

    public static void showIptablesRules(Context ctx) {
        try {
            final StringBuilder res = new StringBuilder();
            runScriptAsRoot("iptables -L/n", res);
            alert(ctx, res);
        } catch (Exception e) {
            alert(ctx, "error: " + e);
        }
    }

    public static DroidApp[] getApps(Context context) {
        if(applications != null) {
            return applications;
        }
        hastether = null;

        final String savedNames = context.getSharedPreferences(PREFS_NAME,0).getString(PREF_ALLOWEDUIDS,"");
        String allowed[];
        if(savedNames.length()>0) {
            final StringTokenizer tok = new StringTokenizer(savedNames,"|");
            allowed = new String[tok.countTokens()];
            for(int i=0;i<allowed.length;i++) {
                allowed[i] = tok.nextToken();
            }
            Arrays.sort(allowed);
        } else {
            allowed = new String[0];
        }
        try {
            final PackageManager pkgmanager = context.getPackageManager();
            final List<ApplicationInfo>  installed = pkgmanager.getInstalledApplications(0);
            final HashMap<Integer,DroidApp> map = new HashMap<Integer,DroidApp>();
            String name;
            DroidApp app;
            for(final ApplicationInfo applicationInfo:installed) {
                if((applicationInfo.flags & ApplicationInfo.FLAG_INSTALLED)>0) {
                    app = map.get(applicationInfo.uid);
                    name = pkgmanager.getApplicationLabel(applicationInfo).toString();
                    if(applicationInfo.packageName.equals("android.tether")) {
                        hastether = name;
                    }
                    if(app == null) {
                        app = new DroidApp();
                        app.uid = applicationInfo.uid;
                        app.username = pkgmanager.getNameForUid(applicationInfo.uid);
                        app.names = new String[]{name};
                    } else {
                        final String newnames[] = new String[app.names.length+1];
                        newnames[app.names.length] = name;////????
                        app.names = newnames;
                    }
                    if(!app.selected && Arrays.binarySearch(allowed,app.username)>=0) {
                        app.selected = true;
                    }
                    map.put(app.uid,app);//save all appinfo
                }
            }
            final DroidApp special[] = {
                    new DroidApp(android.os.Process.getUidForName("root"),"root","(Applications ruuning as root)",false),
                    new DroidApp(android.os.Process.getUidForName("media"),"media","Media server",false),
            };
            for(int i=0;i<special.length;i++) {
                app = special[i];
                if(app.uid != -1 && !map.containsKey(app.uid)) {
                    if(Arrays.binarySearch(allowed,app.username)>=0) {
                        app.selected = true;
                    }
                    map.put(app.uid,app);
                }
            }
            applications = new DroidApp[map.size()];
            int index = 0;
            for(DroidApp application:map.values()) {
                applications[index++] = application;
            }
            return applications;
        } catch (Exception e) {
            alert(context,"error:"+e);
        }
        return null;
    }


    public static boolean hasRootAcess(Context context) {
        if(hasroot){
            return true;
        }
        try{
            if(runScriptAsRoot("exit 0",null,20000)==0) {
                hasroot = true;
                return true;
            }
        }catch (Exception e) {

        }
        alert(context,"Could not acquire root access./n"
        +"You need a rooted phone to run Droid Wall./n/n"
        +"If this phone is already rooted,please make sure Droid Wall has enough permissions to excute the /\"su/\" command.");
        return false;
    }

    public static int runScriptAsRoot(String script,StringBuilder res,final long timeout) {

        final ScriptRunner runner = new ScriptRunner(script,res);
        runner.start();
        try {
            if(timeout >0) {
                runner.join(timeout);
            }else {
                runner.join();
            }
            if(runner.isAlive()) {
                runner.interrupt();
                runner.destroy();
                runner.join(50);
            }
        }catch (InterruptedException e) {

        }
        return runner.exitcode;
    }

    public static int runScriptAsRoot(String script,StringBuilder res) throws IOException{
        return runScriptAsRoot(script,res,20000);
    }


    public static final class DroidApp {
        int uid;
        String username;
        String names[];
        boolean selected;
        String tostr;
        public DroidApp(){

        }

        public DroidApp(int uid,String username, String names,boolean selected) {
            this.username = username;
            this.selected = selected;
            this.names = new String[]{names};
            this.uid = uid;
        }

        @Override
        public String toString() {
            if(tostr == null) {
                final StringBuilder s = new StringBuilder(uid+":");
                for(int i=0;i<names.length;i++) {
                    if(i!=0) {
                        s.append(", ");
                    }
                    s.append(names[i]);
                }
                tostr = s.toString();
            }
            return tostr;
        }
    }


    private static final class ScriptRunner extends Thread {
        private final String script;
        private final StringBuilder res;
        public int exitcode = -1;
        private java.lang.Process exec;
        private Context context;

        final File file = new File(mContext.getCacheDir(),SCRIPT_FILE);

        public ScriptRunner(String script,StringBuilder res) {
            this.script = script;
            this.res = res;
        }

        @Override
        public void run() {
            try {
                file.createNewFile();
                final String abspath = file.getAbsolutePath();
//                String cmdscript = "iptables -L";
                exec = Runtime.getRuntime().exec("su");
                final OutputStreamWriter out = new OutputStreamWriter(exec.getOutputStream());
                if(new File("/system/bin/sh").exists()) {
                    out.write("#!/system/bin/sh\n");
                }
                   out.write(script);

                   if(!script.endsWith("/n")) {
                       out.write("/n");
                   }
                   out.write("exit/n");
                   out.flush();
                    out.close();
                   final char buf[] = new char[1024];
                   InputStreamReader r;
                   r=new InputStreamReader(exec.getInputStream());

                   int read = 0;
                   while ((read = r.read(buf)) != -1) {
                       if(res != null) {
                           res.append(buf,0,read);
                       }
                   }
                   InputStreamReader rError = null;
                   rError = new InputStreamReader(exec.getErrorStream());
                   read = 0;
                   while((read = rError.read(buf)) != -1) {
                       if(res != null) {
                           res.append(buf,0,read);
                       }
                   }
                   if(exec != null) {
                       this.exitcode = exec.waitFor();
                       Log.e("TAG","exitCode:"+exitcode);
                   }
            } catch (InterruptedException e){
                if(res != null) res.append("\nOperation timed-out");
            }catch (Exception e) {
                if(res != null) res.append("\n"+e);
            }finally {
                destroy();
            }

        }
        public synchronized void destroy() {
            if(exec != null) {
                exec.destroy();
            }
            exec = null;
        }

    }


    public static boolean RootCommand(String rootCmd) {
        Process proc = null;
        DataOutputStream out = null;
        try {
            proc = Runtime.getRuntime().exec("sh");
            out = new DataOutputStream(proc.getOutputStream());
            out.writeBytes(rootCmd+"\n");
            out.writeBytes("exit\n");
            out.flush();
            proc.waitFor();

        }catch (Exception e) {
            Log.d("TAG","ewq+++++++++++++:"+e.getMessage());
            return false;
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                proc.destroy();
            }catch (Exception e) {

            }
        }
        Log.d("TAG","Root Suc");
        return true;
    }


}
