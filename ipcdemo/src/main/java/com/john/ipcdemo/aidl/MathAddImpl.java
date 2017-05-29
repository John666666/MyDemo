package com.john.ipcdemo.aidl;

import android.os.RemoteException;

import com.john.ipcdemo.util.LogUtil;

/**
 * Created by John on 2017/5/28.
 */

public class MathAddImpl extends IMathAdd.Stub {
    private static final String TAG = "MathAddImpl";
    @Override
    public int add(int a, int b) throws RemoteException {
        LogUtil.i(TAG, "客户端请求计算: "+a+"+"+b);
        return a + b;
    }
}
