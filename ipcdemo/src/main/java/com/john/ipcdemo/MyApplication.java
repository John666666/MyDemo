package com.john.ipcdemo;

import android.app.Application;
import android.content.Context;

import com.john.ipcdemo.util.LogUtil;
import com.john.ipcdemo.util.ProcessUtils;

/**
 * Created by John on 2017/5/28.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = getApplicationContext();
        LogUtil.i(TAG, "onCreate");
        if(ProcessUtils.isMainProcess()) {
            LogUtil.i(TAG, "do somethings only in main process.");
        }
    }

    public static Context getContext() {
        return ctx;
    }
}
