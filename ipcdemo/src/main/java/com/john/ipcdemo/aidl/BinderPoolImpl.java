package com.john.ipcdemo.aidl;

import android.os.IBinder;
import android.os.RemoteException;

import com.john.ipcdemo.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 2017/5/29.
 */

public class BinderPoolImpl extends IBinderPool.Stub {

    private static final String TAG = "BinderPoolImpl";

    public static final int BIND_MATH_ADD_CODE = 1;
    public static final int BIND_HELLO_CODE = 2;

    static Map<Integer, IBinder> binderPool = new HashMap<>();

    public BinderPoolImpl() {
        registerBinder(BIND_MATH_ADD_CODE, new MathAddImpl());
        registerBinder(BIND_HELLO_CODE, new HelloImpl());
    }

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {

        LogUtil.i(TAG, "客户端请求调用服务端Binder, code: "+binderCode);
        IBinder binder = binderPool.get(binderCode);
        if(binder == null) {
            LogUtil.w(TAG, "BinderPool中未找到匹配的Binder!");
        } else {
            LogUtil.i(TAG, "返回对应的Binder: "+binder);
        }
        return binder;
    }

    public static void registerBinder(Integer binderCode, IBinder binder) {
        binderPool.put(binderCode, binder);
    }

    public static IBinder unregisterBinder(Integer binderCode) {
        return binderPool.remove(binderCode);
    }
}
