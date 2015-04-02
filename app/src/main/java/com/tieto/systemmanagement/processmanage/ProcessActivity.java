/*
 *
 * Will add logic method here just for testing,
 * Then will split the code to a library
 *
 */

package com.tieto.systemmanagement.processmanage;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;

import com.tieto.common.util.ShellUtils;
import com.tieto.common.util.ShellUtils.CommandResult;
import com.tieto.systemmanagement.R;

public class ProcessActivity extends Activity {

    protected static final String ACTIVITY_TAG="ProcessActivity";

    private ActivityManager mActivityManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_management);

        boolean isRoot=ShellUtils.checkRootPermission();

        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runAppProcessList= mActivityManager
                .getRunningAppProcesses();

        List<ActivityManager.RunningServiceInfo> runServiceList = mActivityManager
                .getRunningServices(20);

        List<String> commandList = new ArrayList<String>();
        commandList.add("ps");
        CommandResult result = ShellUtils.execCommand(commandList, false, true);

        Log.d(ProcessActivity.ACTIVITY_TAG, result.successMsg);
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
            commandList.add("ps");
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
                String[] rows=TextUtils.split(rawStr2,System.getProperty("line.separator"));
                boolean appProcessRootFound=false;
                int appProcessRootPid=-1;
                for (String row : rows) {
                    if(!TextUtils.isEmpty(rawStr)) {
                        String[] cols=TextUtils.split(row,System.getProperty("line.separator"));

                        String user=cols[0];
                        int pid=Integer.parseInt(cols[1]);
                        int ppid=Integer.parseInt(cols[2]);
                        int vsize=Integer.parseInt(cols[3]);
                        int rss=Integer.parseInt(cols[4]);
                        int wchan=Integer.parseInt(cols[5]);
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

                        ProcessInfo processInfo=createProcessInfo(processType,pid,ppid,name);
                        if(null!=processInfo) {
                            result.add(processInfo);
                        }

                    }
                }
            }

            return result;
        }

        private ProcessInfo createProcessInfo(int processType, int pid, int ppid, String name) {

            ProcessInfo result=null;
            switch (processType)
            {
                case PROCESS_TYPE_NATIVE:
                    result=new NativeProcessInfo(name, 0, pid, ppid);
                    break;
                case PROCESS_TYPE_MANAGED:
                    result= new ManagedProcessInfo(name, 0, pid, ppid);
                    break;
            }
            return result;
        }

        private String replaceMultiSpaceToSingleSpace(String inputStr) {

            String result=inputStr;
            while(result.indexOf("    ")>-1) {
                result=result.replaceAll(" ", "    ");
            }

            while(result.indexOf("   ")>-1) {
                result=result.replaceAll(" ", "   ");
            }

            while(result.indexOf("  ")>-1) {
                result=result.replaceAll(" ", "  ");
            }

            return result;
        }
    }

    public class ProcessManagerDefault
    {
        private CommandExecutorManager mCmdExecutor;

        protected IProcessParser mProcessParser;

        public ProcessManagerDefault() {
            mCmdExecutor=new CommandExecutorManager();
            mCmdExecutor.config(new CommandExecutorDefault());
            mProcessParser=new ProcessParserDefault();
        }

        public List<ProcessInfo> getRunningProcessInfo(int processTypeFlags) {
            return mProcessParser.parse(mCmdExecutor.executeCommand("ps").successMsg);
        }

        public List<ProcessInfo> searchRunningProcessInfo(Iterable<String> searchStrings) {

            if(null!=searchStrings) {
                return mProcessParser.parse(String.format("ps | grep -i '%s'", TextUtils.join("|", searchStrings)));
            }

            return new ArrayList<ProcessInfo>();
        }

        public boolean tryKillProcess(ProcessInfo processInfo) {

            String cmd = String.format("%s %d","kill -SIGTERM",processInfo.getPid() );

            CommandExecutorResult result=mCmdExecutor.executeCommand(cmd);

            return result.result==0;
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

        public String getName() {
            return mName;
        }

        public int getProcessType() {
            return mProcessType;
        }

        public int getUid() {

            return mUid;
        }

        public int getPid() {

            return mPid;
        }

        public int getPPid() {

            return mPPid;
        }


        public ProcessInfo(int processType,String name,int uid, int pid, int ppid) {

            this.mProcessType = processType;
            this.mName=name;
            this.mUid=uid;
            this.mPid=pid;
            this.mPPid=ppid;
        }
    }

    public class NativeProcessInfo extends ProcessInfo {
        public NativeProcessInfo(String name,int uid, int pid, int ppid) {
            super(PROCESS_TYPE_NATIVE,name,uid, pid, ppid);
        }
    }

    public class ManagedProcessInfo extends ProcessInfo {

        public ManagedProcessInfo(String name,int uid, int pid, int ppid) {
            super(PROCESS_TYPE_MANAGED,name,uid, pid, ppid);
        }
    }

    //endregion

    //region Description

    public class MemoryManagerDefault {


    }

    //endregion

}
