package com.john.mydemo.event.listener;

import com.blankj.utilcode.utils.LogUtils;
import com.john.mydemo.event.observer.PowerKeyObserver;

/**
 * Created by John on 2017/12/12.
 */

public class PowerKeyListener implements PowerKeyObserver.IPowerKeyListener {

    private final static String TAG = "PowerKeyListener";

    @Override
    public void onKeyDown() {
        LogUtils.i(TAG, "power key down...");
    }
}
