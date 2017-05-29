package com.john.ipcdemo.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.john.ipcdemo.service.BindPoolService;
import com.john.ipcdemo.util.LogUtil;

import java.util.concurrent.CountDownLatch;

/**
 * Created by John on 2017/5/29.
 */

public class BinderPool {
    private static final String TAG = "BinderPool";
    private static BinderPool mbinderPool;
    private Context ctx;
    private IBinderPool binderPoolAIDL;
    private CountDownLatch countDown;
    private BinderPool(Context ctx) {
        this.ctx = ctx;
        bind2BinderPoolService();
    }

    public static BinderPool getInstance(Context ctx) {
        if(mbinderPool == null) {
            synchronized (BinderPool.class) {
                if(mbinderPool == null) {
                    mbinderPool = new BinderPool(ctx);
                }
            }
        }
        return mbinderPool;
    }

    /**
     * 按binderCode获取对应Binder
     * @param binderCode
     * @return
     */
    public IBinder getBinder(int binderCode) {
        IBinder binder = null;
        if(binderPoolAIDL == null) {
            LogUtil.i(TAG, "binderPool服务未连接");
        } else {
            try {
                binder = binderPoolAIDL.queryBinder(binderCode);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "queryBinder远程方法调用异常", e);
            }
        }
        return binder;
    }

    private synchronized void bind2BinderPoolService() {
        countDown = new CountDownLatch(1);
        Intent intent = new Intent(this.ctx, BindPoolService.class);
        this.ctx.bindService(intent, sc, Context.BIND_AUTO_CREATE);
        try {
            countDown.await();
//            Thread.sleep(500);
        } catch (InterruptedException e) {
            LogUtil.e(TAG, "连接到BinderPool服务超时");
        }
    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binderPoolAIDL = IBinderPool.Stub.asInterface(service);
            try {
                service.linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            countDown.countDown();
            LogUtil.i(TAG, "连接到BinderPool服务");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.w(TAG, "远程BinderPool服务连接断开");
            binderPoolAIDL = null;
        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            LogUtil.w(TAG, "BinderPool服务挂了");
            binderPoolAIDL.asBinder().unlinkToDeath(this, 0);
            binderPoolAIDL = null;
            bind2BinderPoolService();
        }
    };
}
