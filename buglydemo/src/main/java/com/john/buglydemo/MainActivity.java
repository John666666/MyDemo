package com.john.buglydemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends Activity {

    public static List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        CrashReport.testJavaCrash();
//        CrashReport.testANRCrash();
//        CrashReport.testNativeCrash();
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    String uuid = UUID.randomUUID().toString();
                    Log.i("MainActivity", uuid);
                    list.add(uuid);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
