package com.john.dinghelper;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.utils.Utils;

/**
 * Created by john on 16/10/21.
 */
public class MyApplication extends Application {


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Utils.init(context);
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }

    public static Context getContext() {
        return context;
    }

}
