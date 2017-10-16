package com.example.app;

/**
 * Created by 635901193 on 2017/10/16.
 */

public interface DownloadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
