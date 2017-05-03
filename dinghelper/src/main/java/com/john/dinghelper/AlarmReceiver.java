package com.john.dinghelper;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;

import com.blankj.utilcode.utils.LogUtils;

public class AlarmReceiver extends BroadcastReceiver {

    //获取电源管理器对象
    PowerManager pm;
    // 键盘锁
    KeyguardManager km;
    //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
    PowerManager.WakeLock wl;
    //音量管理
    AudioManager am;
    int streamMusicVolume, streamRingVolume;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //锁定屏幕
                    km = (KeyguardManager) MyApplication.getContext().getSystemService(Context.KEYGUARD_SERVICE);
                    KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
                    kl.reenableKeyguard();
                    break;
                case 1:
                    //恢复声音
                    am.setStreamVolume(AudioManager.STREAM_RING, streamRingVolume, AudioManager.FLAG_SHOW_UI);
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, streamMusicVolume, AudioManager.FLAG_SHOW_UI);
                default:
                    break;
            }
        }
    };

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        km = (KeyguardManager) MyApplication.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        pm = (PowerManager) MyApplication.getContext().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        LogUtils.i("收到定时器广播");

        //解锁
        kl.disableKeyguard();

        //点亮屏幕
        brightScreen();

        //静音
        streamMusicVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
        streamRingVolume = am.getStreamVolume(AudioManager.STREAM_RING);
        am.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_SHOW_UI);

        //启动
        DingDingHelper.startDingDing(context);

        //1秒后重新启动锁屏
        handler.sendEmptyMessageDelayed(0, 1000);
        //10秒后恢复声音
        handler.sendEmptyMessageDelayed(1, 10000);
    }

    /**
     * 点亮屏幕
     */
    private void brightScreen() {
        if (!pm.isScreenOn()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        // 点亮屏幕
                        wl.acquire();
                        // 释放
                        wl.release();
                        wl = null;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
