package com.john.breadpointresume;

import android.os.Handler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by John on 2017/4/26.
 */

public class DownloadUtil {

    boolean downloading = false;
    long currentPos = 0;

    public void download(String url, String dest, final Handler handler) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder request = new Request.Builder().url(url);
        if(currentPos > 0) {
            request.header("Range", "bytes="+currentPos+"-");
        }
        client.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 response.body().contentLength();
            }
        });
    }
}
