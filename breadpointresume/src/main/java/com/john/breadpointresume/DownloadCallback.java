package com.john.breadpointresume;

import java.io.File;

/**
 * Created by John on 2017/4/26.
 */

public interface DownloadCallback {

    /**
     * 下载异常回调
     * @param e
     * @param currentPos    本次下载，当前位置
     * @param contentLength 本次下载请求的contentLength
     */
    void onException(Exception e, long currentPos, long contentLength);

    /**
     * 下载进度回调
     * @param currentPos 本次下载，当前位置
     * @param contentLength 本次下载请求的contentLength
     */
    void onProgress(long currentPos, long contentLength);

    /**
     * 请求到contentLength回调
     * @param contentLength
     */
    void onContentLength(long contentLength);

    /**
     * 开始下载回调
     * @param url
     * @param dest
     */
    void onStart(String url, String dest, long offset);

    /**
     * 暂停下载
     * @param currentPos
     * @param contentLength
     */
    void onPause(long currentPos, long contentLength);

    /**
     * 下载成功
     * @param destFile 保存到本地的文件
     */
    void onFinish(File destFile);
}
