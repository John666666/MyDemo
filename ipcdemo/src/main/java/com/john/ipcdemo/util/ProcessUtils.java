package com.john.ipcdemo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import com.john.ipcdemo.MyApplication;

import java.util.List;

/**
 * Created by John on 2017/5/28.
 */

public class ProcessUtils {

    public static String getCurrentProcessName() {
        Context ctx = MyApplication.getContext();
        if(ctx == null) {
            return "";
        }
        int pid = Process.myPid();
        return getProcessNameByPid(ctx, pid);
    }

    public static String getProcessNameByPid(Context ctx, int pid) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        if(processInfos == null || processInfos.size() < 1) {
            return null;
        }
        for(ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if(processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }

    public static boolean isMainProcess() {
        String processName = getCurrentProcessName();
        if(TextUtils.isEmpty(processName)) return false;
        return processName.equals(MyApplication.getContext().getPackageName());
    }

}
