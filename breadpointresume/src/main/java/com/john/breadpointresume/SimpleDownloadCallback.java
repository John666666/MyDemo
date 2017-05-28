package com.john.breadpointresume;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;

/**
 * Created by John on 2017/4/26.
 */

public class SimpleDownloadCallback implements DownloadCallback {

    private static final String TAG = "SimpleDownloadCallback";

    private Handler handler;
    private long startPos = 0;
    public static final int DOWNLOAD_SUCCESS = 1;
    public static final int DOWNLOAD_PROGRESS = 2;
    public static final int DOWNLOAD_FAIL = 3;
    public static final int DOWNLOAD_PAUSE = 4;

    public SimpleDownloadCallback(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onException(Exception e, long currentPos, long contentLength) {
        Log.e(TAG, "下载失败, " + (currentPos + this.startPos) + "/" + (this.startPos + contentLength), e);
        Message msg = Message.obtain();
        msg.what = DOWNLOAD_FAIL;
        DownloadResult result = new DownloadResult();
        result.currentPos = currentPos + this.startPos;
        result.totalLength = contentLength + this.startPos;
        result.e = e;
        result.message = "下载出错";
        msg.obj = result;
        handler.sendMessage(msg);
    }

    @Override
    public void onProgress(long currentPos, long contentLength) {
        Message msg = Message.obtain();
        msg.what = DOWNLOAD_PROGRESS;
        DownloadResult result = new DownloadResult();
        result.currentPos = currentPos + this.startPos;
        result.totalLength = contentLength + this.startPos;
        result.message = "下载进度";
        int progress = (int)(((double)result.currentPos) * 100 / result.totalLength);
        result.progressPercent = progress;
        msg.obj = result;
        handler.sendMessage(msg);
        Log.i(TAG, "下载进度, "+result.progressPercent+"% " + result.currentPos + "/" + result.totalLength);
    }

    @Override
    public void onContentLength(long contentLength) {
        Log.i(TAG, "本次需下载数据大小: " + contentLength);
    }

    @Override
    public void onStart(String url, String dest, long offset) {
        this.startPos = offset;
        if (offset == 0) {
            File file = new File(dest);
            if (file.exists()) {
                file.delete();
                Log.i(TAG, "delete old file.");
            }
        }
    }

    @Override
    public void onPause(long currentPos, long contentLength) {
        Log.i(TAG, "暂停下载: " + (currentPos + this.startPos) + "/" + (contentLength + this.startPos));
        handler.sendEmptyMessage(DOWNLOAD_PAUSE);
    }

    @Override
    public void onFinish(File destFile) {
        Log.i(TAG, "下载完成: " + destFile.getAbsoluteFile());
        handler.sendEmptyMessage(DOWNLOAD_SUCCESS);
    }

    static class DownloadResult {
        public long currentPos;
        public long totalLength;
        public int progressPercent;
        public Exception e;
        public String message;
    }
}
