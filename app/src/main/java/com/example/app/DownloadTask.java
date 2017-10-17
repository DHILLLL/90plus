package com.example.app;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 635901193 on 2017/10/16.
 */

class DownloadTask extends AsyncTask<String, Integer, Integer> {
    private static final String TAG = "dong";
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_CANCELED = 2;
    public static final int TYPE_PAUSED = 3;

    private com.example.app.DownloadListener listener;

    private boolean isCanceled = false;
    private boolean isPaused = false;

    private int lastProgress;

    public DownloadTask(com.example.app.DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            long downloadLength = 0;
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()){
                file.delete();
            }
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0){
                return TYPE_FAILED;
            }else if (contentLength == downloadLength){
                return TYPE_SUCCESS;
            }

            GetVersionInfoFromDB getVersionInfoFromDB = new GetVersionInfoFromDB();
            getVersionInfoFromDB.connAndGetVersionInfo();
            long myLength = (long) (getVersionInfoFromDB.getFileSize() * 1024 * 1024);
            Log.d(TAG, String.valueOf(getVersionInfoFromDB.getFileSize()));
            Log.d(TAG, String.valueOf(myLength));

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().addHeader("RANGE","bytes=" + downloadLength + "-").url(downloadUrl).build();
            Response response = client.newCall(request).execute();
            if (request != null){
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file,"rw");
                savedFile.seek(downloadLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while((len = is.read(b)) != -1 ){
                    if (isCancelled()){
                        return TYPE_FAILED;
                    }else {
                        total += len;
                        savedFile.write(b,0,len);
                        int progress = (int) ((total + downloadLength) * 100 / myLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (is != null){
                    is.close();
                }
                if (savedFile != null){
                    savedFile.close();
                }
                if (isCancelled() && file != null){
                    file.delete();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if(progress > lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    public void cancelDownload(){
        isCanceled = true;
    }

    public void pauseDownload(){
        isPaused = true;
    }

    private long getContentLength(String downloadUrl) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
}