/*
 *
 * Will add logic method here just for testing,
 * Then will split the code to a library
 *
 */

package com.tieto.systemmanagement.processmanage;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.text.TextUtils;
import android.util.Log;
import android.os.Handler;
import android.os.Debug.MemoryInfo;

import java.lang.Thread;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.tieto.common.util.ShellUtils;
import com.tieto.common.util.ShellUtils.CommandResult;
import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.processmanage.views.SlidingChartView;

public class ProcessActivity extends Activity {

    protected static final String ACTIVITY_TAG="ProcessActivity";

    private Random random=new Random();
    private SlidingChartView mCharView=null;
    private Handler mMainHandler =null;
    private ProcessManagerDefault mProcessMgr=null;
    private ResourceUsageThread mResourceUsageThread=null;
    private ResourceInfo mResourceInfo=new ResourceInfo();

    public static final int WHAT_UPDATE_CPU_INFO=1;
    public static final int WHAT_UPDATE_MEM_INFO=2;

    private class ResourceInfo {
        public int cpuSpeed;
        public float cpuUsage;
        public int memoryUsageInKB;
    }

    private class ResourceUsageThread extends Thread {

        private Handler mResourceUsageHandler = null;

        private static final String CHILD_TAG = "CPUUsageThread";

        public Handler getHandler() {
            return mResourceUsageHandler;
        }

        public void run() {
            this.setName("CPUUsageThread");

            Looper.prepare();

            mResourceUsageHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Message toMain = new Message();

                    mResourceInfo.cpuUsage= mProcessMgr.getCpuUsage();
                    toMain.obj=mResourceInfo;
                    mMainHandler.sendMessage(toMain);
                }
            };

            mResourceUsageHandler.sendEmptyMessageDelayed(
                    WHAT_UPDATE_CPU_INFO|WHAT_UPDATE_MEM_INFO,1500);

            Looper.loop();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(null!=mCharView) {
                float val=random.nextFloat()*mCharView.getYRange();
                mCharView.append(val);
                Log.d(ProcessActivity.ACTIVITY_TAG,"new char val="+val);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_management);

        mCharView=(SlidingChartView)findViewById(R.id.chart_cpu);

        mProcessMgr=new ProcessManagerDefault();

        mResourceUsageThread=new ResourceUsageThread();
        mResourceUsageThread.start();

        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                ResourceInfo info=(ResourceInfo)msg.obj;

                if(null!=mCharView) {
                    float val=info.cpuUsage*mCharView.getYRange();
                    mCharView.append(val);
                    Log.d(ProcessActivity.ACTIVITY_TAG,"new cpu val="+val);
                }

                mResourceUsageThread.getHandler().sendEmptyMessageDelayed(
                        WHAT_UPDATE_CPU_INFO|WHAT_UPDATE_MEM_INFO,1500);
                //mMainHandler.postDelayed(runnable, 1500);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_process_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region Command Execution

    public class CommandExecutorManager {
        private ICommandExecutor mCommandExecutor;

        public void config(ICommandExecutor commandExecutor)
        {
            if(null!=commandExecutor)
            {
                commandExecutor.config(this);
                this.mCommandExecutor=commandExecutor;
            }
        }

        public CommandExecutorResult executeCommand(String command)
        {
            return  this.mCommandExecutor.execute(command);
        }
    }

    public interface ICommandExecutor
    {
        void config(CommandExecutorManager commandExecutorManager);

        CommandExecutorResult execute(String command);
    }

    public static class CommandExecutorResult {

        /** result of command **/
        public int    result;
        /** success message of command result **/
        public String successMsg;
        /** error message of command result **/
        public String errorMsg;

        public CommandExecutorResult(int result) {
            this.result = result;
        }

        public CommandExecutorResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }

    public class CommandExecutorDefault implements ICommandExecutor {

        public void config(CommandExecutorManager commandExecutorManager) {

        }

        public CommandExecutorResult execute(String command) {
            List<String> commandList = new ArrayList<String>();
            commandList.add(command);
            CommandResult result = ShellUtils.execCommand(commandList, false, true);

            return new CommandExecutorResult(result.result, result.successMsg, result.errorMsg);
        }
    }
    //endregion

    //region Process Management

    public interface IProcessParser
    {
        List<ProcessInfo> parse(String rawStr);
    }

    public class ProcessParserDefault implements IProcessParser {

        private static final String appProcessRoot="zygote";

        public List<ProcessInfo> parse(String rawStr) {
            List<ProcessInfo> result=new ArrayList<ProcessInfo>();
            //USER     PID   PPID  VSIZE  RSS     WCHAN    PC        NAME

            if(!TextUtils.isEmpty(rawStr))
            {
                String rawStr2= replaceMultiSpaceToSingleSpace(rawStr);
                String[] rows=TextUtils.split(rawStr2,"\n");
                boolean appProcessRootFound=false;

                int appProcessRootPid=-1;
                for (int i=1;i<rows.length; i++) {
                    if(!TextUtils.isEmpty(rawStr)) {
                        String[] cols=TextUtils.split(rows[i]," ");

                        if(cols.length>7) {
                            String user=cols[0];
                            int pid=Integer.parseInt(cols[1]);
                            int ppid=Integer.parseInt(cols[2]);
                            int vsize=Integer.parseInt(cols[3]);
                            int rss=Integer.parseInt(cols[4]);

                            String type=cols[7];
                            String name=cols[8];

                            int processType=PROCESS_TYPE_NATIVE;

                            if(!appProcessRootFound) {
                                if(appProcessRoot.equalsIgnoreCase(name)) {
                                    appProcessRootFound=true;
                                    appProcessRootPid=pid;
                                }
                            }
                            else {
                                if(ppid==appProcessRootPid) {
                                    processType=PROCESS_TYPE_MANAGED;
                                }
                            }

                            ProcessInfo processInfo=createProcessInfo(processType,pid,ppid,name,vsize,rss);
                            if(null!=processInfo) {
                                result.add(processInfo);
                            }
                        }
                    }
                }
            }

            return result;
        }

        private ProcessInfo createProcessInfo(int processType, int pid, int ppid, String name,int vsize,int rss) {

            ProcessInfo result=null;
            switch (processType)
            {
                case PROCESS_TYPE_NATIVE:
                    result=new NativeProcessInfo(name, 0, pid, ppid, vsize, rss);
                    break;
                case PROCESS_TYPE_MANAGED:
                    result= new ManagedProcessInfo(name, 0, pid, ppid, vsize, rss);
                    break;
            }
            return result;
        }

        private String replaceMultiSpaceToSingleSpace(String inputStr) {
            return inputStr.replaceAll(" {2,}"," ");
        }
    }

    public class ProcessManagerDefault
    {
        private CommandExecutorManager mCmdExecutor;
        private IProcessParser mProcessParser;
        private ActivityManager mActivityManager = null;

        public ProcessManagerDefault() {
            mCmdExecutor=new CommandExecutorManager();
            mCmdExecutor.config(new CommandExecutorDefault());
            mProcessParser=new ProcessParserDefault();
            mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        }

        public List<ProcessInfo> getRunningProcessInfo(int processTypeFlags) {
            return mProcessParser.parse(mCmdExecutor.executeCommand("ps").successMsg);
        }

        public List<ProcessInfo> searchRunningProcessInfo(Iterable<String> searchStrings) {

            if(null!=searchStrings) {

                CommandExecutorResult res= mCmdExecutor.executeCommand(String.format("ps | grep -i '%s'", TextUtils.join("|", searchStrings)));

                return mProcessParser.parse(res.successMsg);
            }

            return new ArrayList<ProcessInfo>();
        }

        public boolean tryKillProcess(ProcessInfo processInfo) {

            if(isRoot()) {
                String cmd = String.format("%s %d","kill -SIGTERM",processInfo.getPid() );
                CommandExecutorResult result=mCmdExecutor.executeCommand(cmd);
                return result.result==0;
            } else {

                String packageName=getPackageNameByPID(processInfo.getPid());
                if(null!=packageName) {

                    mActivityManager.killBackgroundProcesses(packageName);

                    CommandExecutorResult res= mCmdExecutor.executeCommand(String.format("ps | grep -i '%s'", processInfo.getName()));

                    if(null!=res.successMsg&&
                            res.successMsg.indexOf(processInfo.getName()) >=0 &&
                            res.successMsg.indexOf(" "+processInfo.getPid()+" ")>0) {
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean isRoot() {

            CommandExecutorResult result=mCmdExecutor.executeCommand("echo root");

            return result.result==0;
        }

        public float getCpuUsage() {

            String cmd = "cat /proc/stat | grep \'cpu \'";
            CommandExecutorResult result=mCmdExecutor.executeCommand(cmd);

            String raw= result.successMsg.trim().replaceAll(" {2,}"," ");
            String[] cols=raw.split(" ");

            int total1=0;
            int idle1=0;
            for(int i=1;i<cols.length;i++) {
                total1+=Integer.parseInt(cols[i]);
            }

            idle1=Integer.parseInt(cols[3]);

            try {
                Thread.sleep(1500);
            } catch (Exception e) {

            }

            result=mCmdExecutor.executeCommand(cmd);
            raw= result.successMsg.trim().replaceAll(" {2,}"," ");
            cols=raw.split(" ");

            int total2=0;
            int idle2=0;
            for(int i=1;i<cols.length;i++) {
                total2+=Integer.parseInt(cols[i]);
            }

            idle2=Integer.parseInt(cols[3]);

            int total=total2-total1;
            int idle=idle2-idle1;

            return ((float)(total-idle))/(float)total;
        }

        public float getMemoryUsage() {

            return 0.0f;
        }

        public void getCpuInfo() {

//cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq
        }

        private String getPackageNameByPID(int pid) {

            String packageName=null;
            List<RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
            for(RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                try {
                    if(runningAppProcessInfo.pid == pid) {
                        packageName = runningAppProcessInfo.pkgList[0];
                    }
                } catch(Exception e) {

                }
            }
            return packageName;
        }

        private int getCpuInfoByPID() {

            return 0;
        }

        private int getMemInfoByPID() {
            return 0;
        }
    }
    //endregion

    //region Entity

    public static final int PROCESS_TYPE_NATIVE=1;

    public static final int PROCESS_TYPE_MANAGED=2;

    public abstract class ProcessInfo {

        private String mName;
        private int mProcessType;
        private int mUid;
        private int mPid;
        private int mPPid;
        private int mVSize;
        private int mRSS;

        public String getName() {
            return mName;
        }

        public int getProcessType() {
            return mProcessType;
        }

        public int getUid() {

            return mUid;
        }

        public int getVSize() {

            return mVSize;
        }

        public int getRSS() {

            return mRSS;
        }

        public int getPid() {

            return mPid;
        }

        public int getPPid() {

            return mPPid;
        }


        public ProcessInfo(int processType,String name,int uid, int pid, int ppid,int vSize, int rss) {

            this.mProcessType = processType;
            this.mName=name;
            this.mUid=uid;
            this.mPid=pid;
            this.mPPid=ppid;
            this.mVSize=vSize;
            this.mRSS=rss;
        }
    }

    public class NativeProcessInfo extends ProcessInfo {
        public NativeProcessInfo(String name,int uid, int pid, int ppid,int vSize, int rss) {
            super(PROCESS_TYPE_NATIVE,name,uid, pid, ppid, vSize, rss);
        }
    }

    public class ManagedProcessInfo extends ProcessInfo {

        public ManagedProcessInfo(String name,int uid, int pid, int ppid,int vSize, int rss) {
            super(PROCESS_TYPE_MANAGED,name,uid, pid, ppid, vSize, rss);
        }
    }

    //endregion

    //region Description

    public class MemoryManagerDefault {


    }

    //endregion

}
