package com.john.breadpointresume;

import android.app.Application;
import android.content.Context;

/**
 * Created by John on 2017/4/26.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
