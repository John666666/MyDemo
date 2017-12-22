package com.john.mydemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.utils.LogUtils;
import com.john.mydemo.R;
import com.john.mydemo.view.MyButton;
import com.john.mydemo.view.MyLinearLayout;

public class EventDispatcherActivity extends Activity {

    private final static String TAG = "EventDispatcherActivity";

    private MyButton mButton;
    private MyLinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dispatcher);

        mButton = (MyButton) findViewById(R.id.btn_my_event_button);
        mButton.setOnTouchListener((v, event) -> {
            LogUtils.i(TAG, "button's onTouchListener");
            return false;
        });

        mLinearLayout = (MyLinearLayout) findViewById(R.id.layout_my_linear);
        mLinearLayout.setOnTouchListener((v, event) -> {
            LogUtils.i(TAG, "MyLinearLayout's onTouchListener");
            return false;
        });

    }
}
