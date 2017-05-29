package com.john.ipcdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

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
                    String fromClientMsg = ((Bundle)msg.obj).getString("msg");
                    LogUtil.i(TAG, "receive client msg: " + fromClientMsg);
                    Messenger toClient = msg.replyTo;
                    if(toClient != null) {
                        Message replyMessage = Message.obtain();
                        replyMessage.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", "server: I receive your msg: "+fromClientMsg);
                        replyMessage.obj = bundle;
                        try {
                            toClient.send(replyMessage);
                        } catch (RemoteException e) {
                            LogUtil.e(TAG, "回复客户端出错", e);
                        }
                    }
                    break;
            }
        }
    };
}
