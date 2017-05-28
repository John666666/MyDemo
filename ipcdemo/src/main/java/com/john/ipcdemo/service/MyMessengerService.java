package com.john.ipcdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.john.ipcdemo.aidl.IMyAIDLTest;
import com.john.ipcdemo.aidl.MyAIDLTestImpl;
import com.john.ipcdemo.util.LogUtil;

public class MyMessengerService extends Service {
    private static final String TAG = "MyMessengerService";
    public static final String BIND_CODE_KEY = "code";
    public static final int BIND_MESSENGER_CODE = 1;
    public static final int BIND_AIDL_CODE = 2;

    private Messenger messenger;
    private IMyAIDLTest myAIDLTest;

    @Override
    public void onCreate() {
        super.onCreate();
        messenger = new Messenger(handler);
        myAIDLTest = new MyAIDLTestImpl();
    }

    @Override
    public IBinder onBind(Intent intent) {
        int bindCode = intent.getIntExtra(BIND_CODE_KEY, 0);
        if (bindCode == 0) {
            LogUtil.w(TAG, "没有匹配的binder处理类");
            return null;
        }
        switch (bindCode) {
            case BIND_MESSENGER_CODE:
                return messenger.getBinder();
            case BIND_AIDL_CODE:
                return myAIDLTest.asBinder();
        }
        LogUtil.w(TAG, "没有匹配的binder处理类");
        return null;
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
