package com.john.ipcdemo.aidl;

import android.os.RemoteException;
import android.text.TextUtils;

/**
 * Created by John on 2017/5/29.
 */

public class HelloImpl extends IHello.Stub{
    @Override
    public String sayHello(String name) throws RemoteException {
        if(!TextUtils.isEmpty(name)) {
            return "Hello! "+name+" nice to meet you!";
        }
        return "What's your name, please?";
    }
}
