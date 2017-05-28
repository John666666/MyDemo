package com.john.breadpointresume;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by John on 2017/4/26.
 */

public class DownloadUtil {
    private static final String TAG  = "DownloadUtil";
    private static final int BUFFER_SIZE = 102400;

    boolean downloading = false;
    long currentPos = 0;
    long contentLength = 0;

    public void download(final String url, final String dest, final DownloadCallback callback) {
        //重置状态，标记位
        downloading = true;
        currentPos = 0;
        contentLength = 0;

        OkHttpClient client = new OkHttpClient();
        Request.Builder request = new Request.Builder().url(url);
        final long lastPos = Long.parseLong(SPUtil.getInstance(MyApplication.getContext()).getValue(url, "0"));
        Log.i(TAG, "lastPos: "+lastPos);
        if(lastPos > 0) {
            request.header("Range", "bytes="+lastPos+"-");
        }
        Log.i(TAG, "begin download offset: "+lastPos);
        callback.onStart(url, dest, lastPos);
        client.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onException(e, currentPos, contentLength);
                SPUtil.getInstance(MyApplication.getContext()).putValue(url, currentPos+"");
                downloading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                contentLength = response.body().contentLength();
                callback.onContentLength(contentLength);
                File destFile = new File(dest);
                if(destFile.getParentFile() != null && !destFile.getParentFile().exists()) {
                    if(!destFile.mkdirs()) {
                        callback.onException(null, currentPos, contentLength);
                        return;
                    }
                }
                InputStream in = response.body().byteStream();
                BufferedOutputStream out = null;
                byte[] buffer = new byte[BUFFER_SIZE];
                int len = -1;
                out = new BufferedOutputStream(new FileOutputStream(destFile, true));
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                    currentPos += len;
                    SPUtil.getInstance(MyApplication.getContext()).putValue(url, (currentPos + lastPos)+"");
                    callback.onProgress(currentPos, contentLength);
                    if(!downloading) {
                        break;
                    }
                }
                if(!downloading) {
                    callback.onPause(currentPos, contentLength);
                } else {
                    callback.onFinish(destFile);
                    downloading = false;
                    SPUtil.getInstance(MyApplication.getContext()).removeValue(url);
                }
                out.flush();
                out.close();
                in.close();
            }
        });
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean flag) {
        this.downloading = flag;
        if(!flag) {
            Log.i(TAG, "暂停下载");
        }
    }
}
