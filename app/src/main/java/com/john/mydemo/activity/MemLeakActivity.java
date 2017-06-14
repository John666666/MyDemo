package com.john.mydemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.john.mydemo.R;

/**
 * 内存泄露测试
 */
public class MemLeakActivity extends Activity {

    private final static String TAG = "MemLeakActivity";

    public final static int TEST_COUNT = 20;

    private int count = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handle message...");
            if(count < TEST_COUNT) {
                sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_leak);

        handler.sendEmptyMessage(1);
    }
}
