package com.john.ipcdemo.util;

import android.util.Log;

/**
 * Created by John on 2017/5/28.
 */

public class LogUtil {
    public static void v(String tag, String msg) {
        Log.v(tag, "["+ProcessUtils.getCurrentProcessName()+"] " + msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, "["+ProcessUtils.getCurrentProcessName()+"] " + msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, "["+ProcessUtils.getCurrentProcessName()+"] " + msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, "["+ProcessUtils.getCurrentProcessName()+"] " + msg);
    }

    public static void e(String tag, String msg, Throwable e) {
        Log.e(tag, "["+ProcessUtils.getCurrentProcessName()+"] " + msg, e);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, "["+ProcessUtils.getCurrentProcessName()+"] " + msg);
    }
}
