package com.john.ipcdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.john.ipcdemo.util.LogUtil;


public class MyMessengerService extends Service {
    private static final String TAG = "MyMessengerService";

    private Messenger messenger;

    @Override
    public void onCreate() {
        super.onCreate();
        messenger = new Messenger(handler);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LogUtil.i(TAG, "receive remote msg: " + ((Bundle)msg.obj).getString("msg"));
                    break;
            }
        }
    };
}
