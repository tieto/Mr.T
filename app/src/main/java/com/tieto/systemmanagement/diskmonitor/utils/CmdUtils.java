package com.tieto.systemmanagement.diskmonitor.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangbo on 4/29/15.
 */
public class CmdUtils {
    private static final String[] OS_LINUX_RUNTIME = { "rm -fr "};
    private static final String TAG = "CmdUtils";
    private CmdUtils() {
    }

    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static List<String> executeCmd(String... command) {
        Log.v(TAG,"command to run: ");
        for (String s : command) {
            Log.v(TAG,(s));
        }
        String[] allCommand = null;
        try {

            allCommand = concat(OS_LINUX_RUNTIME, command);
            ProcessBuilder pb = new ProcessBuilder(allCommand);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String _temp = null;
            List<String> line = new ArrayList<String>();
            while ((_temp = in.readLine()) != null) {
                Log.v(TAG,"temp line: " + _temp);
                line.add(_temp);
            }
            Log.v(TAG,"result after command: " + line);
            return line;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}