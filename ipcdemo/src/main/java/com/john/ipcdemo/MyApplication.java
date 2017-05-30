package com.john.ipcdemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.john.ipcdemo.service.ServerSocketService;
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
            //Æô¶¯ServerSocket·þÎñ
            Intent intent = new Intent(ctx, ServerSocketService.class);
            startService(intent);
        }
    }

    public static Context getContext() {
        return ctx;
    }
}
