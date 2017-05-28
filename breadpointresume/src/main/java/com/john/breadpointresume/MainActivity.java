package com.john.breadpointresume;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends Activity implements View.OnClickListener{

    private ProgressBar progressBar;
    private Button btnStart;
    private Button btnPause;

    private static final String TEST_URL = "http://7xr0c9.media1.z0.glb.clouddn.com/10_%E6%AC%BA%E9%AA%97%E9%AB%98%E6%89%8B.mp3";
    private static final String SAVE_FILE = "/sdcard/download/test.mp3";

    private DownloadUtil downloadUtil = new DownloadUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        btnStart = (Button) findViewById(R.id.start);
        btnPause = (Button) findViewById(R.id.pause);
        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                downloadUtil.download(TEST_URL, SAVE_FILE, new SimpleDownloadCallback(new MyHandler(new WeakReference<MainActivity>(this))));
                break;
            case R.id.pause:
                downloadUtil.setDownloading(false);
                break;
        }
    }

    static class MyHandler extends Handler {

        MainActivity activity;
        public MyHandler(WeakReference<MainActivity> activiyRef) {
            this.activity = activiyRef.get();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SimpleDownloadCallback.DOWNLOAD_SUCCESS:
                    Toast.makeText(activity, "下载完成", Toast.LENGTH_SHORT).show();
                    break;
                case SimpleDownloadCallback.DOWNLOAD_FAIL:
                    Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
                case SimpleDownloadCallback.DOWNLOAD_PAUSE:
                    Toast.makeText(activity, "暂停下载", Toast.LENGTH_SHORT).show();
                    break;
                case SimpleDownloadCallback.DOWNLOAD_PROGRESS:
                    SimpleDownloadCallback.DownloadResult result = (SimpleDownloadCallback.DownloadResult) msg.obj;
                    activity.progressBar.setProgress(result.progressPercent);
                    break;
            }
        }
    }
}
