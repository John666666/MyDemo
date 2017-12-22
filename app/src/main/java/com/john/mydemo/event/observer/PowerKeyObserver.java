package com.john.mydemo.event.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.blankj.utilcode.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 2017/12/12.
 */

public class PowerKeyObserver {

    private final static String TAG = "PowerKeyObserver";

    private Context mContext;
    private List<IPowerKeyListener> mPowerKeyListeners;
    private PowerKeyBroadcastReceiver mPowerKeyBroadcastReceiver;

    public PowerKeyObserver(Context mContext) {
        this.mContext = mContext;
        mPowerKeyBroadcastReceiver = new PowerKeyBroadcastReceiver();
    }

    public PowerKeyObserver(Context mContext, IPowerKeyListener powerKeyListener) {
        this(mContext);
        registerListener(powerKeyListener);
    }

    public void startListen() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mContext.registerReceiver(mPowerKeyBroadcastReceiver, filter);
    }

    public void destory() {
        mContext.unregisterReceiver(mPowerKeyBroadcastReceiver);
    }

    public void registerListener(IPowerKeyListener powerKeyListener) {
        if (mPowerKeyListeners == null) {
            mPowerKeyListeners = new ArrayList<>();
        }
        mPowerKeyListeners.add(powerKeyListener);
    }

    public void unregisterListenr(IPowerKeyListener powerKeyListener) {
        if (powerKeyListener != null) {
            synchronized (mPowerKeyListeners) {
                if (mPowerKeyListeners.contains(powerKeyListener)) {
                    mPowerKeyListeners.remove(powerKeyListener);
                }
            }
        }
    }

    public interface IPowerKeyListener {
        void onKeyDown();
    }


    //广播接收者
    class PowerKeyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.i(TAG, "action: " + action);
            if (mPowerKeyListeners != null) {
                for (IPowerKeyListener powerKeyListener : mPowerKeyListeners) {
                    powerKeyListener.onKeyDown();
                }
            }
        }
    }
}
