package com.john.ipcdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.john.ipcdemo.aidl.BinderPoolImpl;
import com.john.ipcdemo.aidl.IBinderPool;

public class BindPoolService extends Service {

    private IBinderPool binderPool;

    @Override
    public void onCreate() {
        super.onCreate();
        binderPool = new BinderPoolImpl();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binderPool.asBinder();
    }
}
