package com.john.breadpointresume;

import android.os.Handler;

/**
 * Created by John on 2017/4/26.
 */

public class SimpleDownloadCallback implements DownloadCallback {

    private Handler handler;
    private long startPos = 0;

    public SimpleDownloadCallback(long startPos, Handler handler) {
        this.handler = handler;
        this.startPos = startPos;
    }

    @Override
    public void onException(Exception e, long currentPos, long totalLength) {

    }

    @Override
    public void onProgress(long currentPos, long totalLength) {

    }

    @Override
    public void onContentLength(long contentLength) {

    }
}
