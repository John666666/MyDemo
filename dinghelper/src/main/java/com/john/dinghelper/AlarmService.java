package com.john.dinghelper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.utils.LogUtils;

/**
 * Created by john on 17/3/2.
 */

public class AlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("启动AlarmService服务");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DingDingHelper.startAlarm(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);
    }
}
