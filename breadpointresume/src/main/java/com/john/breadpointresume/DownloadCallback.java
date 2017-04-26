package com.john.breadpointresume;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by John on 2017/4/26.
 */

public interface DownloadCallback {

    void onException(Exception e, long currentPos, long totalLength);

    void onProgress(long currentPos, long totalLength);

    void onContentLength(long contentLength);
}
