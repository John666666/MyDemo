package com.john.ipcdemo.aidl;

import android.os.IBinder;
import android.os.RemoteException;

import com.john.ipcdemo.util.LogUtil;

/**
 * Created by John on 2017/5/28.
 */

public class MyAIDLTestImpl extends IMyAIDLTest.Stub {
    private static final String TAG = "MyAIDLTestImpl";
    @Override
    public int add(int a, int b) throws RemoteException {
        LogUtil.i(TAG, "客户端请求计算: "+a+"+"+b);
        return a + b;
    }

    @Override
    public IBinder asBinder() {
        return this;
    }
}
