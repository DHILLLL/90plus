package com.whuLoveStudyGroup.app;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by benjaminzhang on 16/10/2017.
 * Modified by benjaminzhang on 28/01/2018.
 * Copyright © 2018 benjaminzhang.
 * All rights reserved.
 * Version 1.2.1.1801282140
 */

public class GetVersionInfoFromDB {
    private final String PROTOCOL = "http://";
    private final String SERVERADDR = "39.108.108.43";
//    private final String SERVERADDR = "127.0.0.1";
    private final String PORT = "9090";
    private final String DBINFOLATESTADDR = "90plus/api/v1/get/software_info/latest";
    private final String DBINFOALLADDR = "90plus/api/v1/get/software_info/all";
    private Resp resp = null;

    protected void connAndGetVersionInfo() throws UnknownErrorException, NetworkErrorException{
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + DBINFOALLADDR;
        System.out.println(url);
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String respStr = null;
            if (response.body() != null) {
                respStr = response.body().string();
            } else throw new UnknownErrorException();
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, Resp.class);
        } catch (JsonSyntaxException e) {
            throw new UnknownErrorException();
        } catch (ConnectException e) {
            throw new NetworkErrorException();
        } catch (IOException e) {
            throw new UnknownErrorException();
        }
    }

    protected int getLatestVersionID() {
        return resp.data[resp.data.length - 1].version_id;
    }

    protected String getLatestVersion() {
        return resp.data[resp.data.length - 1].version;
    }

    protected String getLatestInfo() {
        return resp.data[resp.data.length - 1].info;
    }

    protected String getLatestChangeLog() {
        return resp.data[resp.data.length - 1].change_log;
    }

    protected boolean isLatestCritical() {
        return resp.data[resp.data.length - 1].critical;
    }

    protected long getLatestFileSize() {
        return resp.data[resp.data.length - 1].file_size;
    }

    protected String getLatestDownloadAddress() {
        return resp.data[resp.data.length - 1].link;
    }

    protected List<UpdateInformation> getUpdateHistory() {
        List<UpdateInformation> list = new ArrayList<>();
        for (int i = resp.data.length - 1; i >= 0; i--) {
            UpdateInformation temp = new UpdateInformation(resp.data[i].version.toString(), resp.data[i].change_log);
            list.add(temp);
        }
        return list;
    }

    private class Resp {
        private String msg = null;
        private int code = 0;
        private VersionInfo[] data = null;
    }

    private class VersionInfo {
        private String change_log = null;
        private boolean critical = false;
        private long file_size = 0;
        private String info = null;
        private String link = null;
        private String version = null;
        private int version_id = 0;
    }

    protected class UnknownErrorException extends Exception {
        public UnknownErrorException() {
            super();
        }

        public UnknownErrorException(String message) {
            super(message);
        }

        public UnknownErrorException(String message, Throwable cause) {
            super(message, cause);
        }

        public UnknownErrorException(Throwable cause) {
            super(cause);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public UnknownErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        @Override
        public String getMessage() {
            return super.getMessage();
        }

        @Override
        public String getLocalizedMessage() {
            return super.getLocalizedMessage();
        }

        @Override
        public synchronized Throwable getCause() {
            return super.getCause();
        }

        @Override
        public synchronized Throwable initCause(Throwable cause) {
            return super.initCause(cause);
        }

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        public void printStackTrace() {
            super.printStackTrace();
        }

        @Override
        public void printStackTrace(PrintStream s) {
            super.printStackTrace(s);
        }

        @Override
        public void printStackTrace(PrintWriter s) {
            super.printStackTrace(s);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return super.fillInStackTrace();
        }

        @Override
        public StackTraceElement[] getStackTrace() {
            return super.getStackTrace();
        }

        @Override
        public void setStackTrace(StackTraceElement[] stackTrace) {
            super.setStackTrace(stackTrace);
        }
    }

    protected class NetworkErrorException extends Exception {
        public NetworkErrorException() {
            super();
        }

        public NetworkErrorException(String message) {
            super(message);
        }

        public NetworkErrorException(String message, Throwable cause) {
            super(message, cause);
        }

        public NetworkErrorException(Throwable cause) {
            super(cause);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public NetworkErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        @Override
        public String getMessage() {
            return super.getMessage();
        }

        @Override
        public String getLocalizedMessage() {
            return super.getLocalizedMessage();
        }

        @Override
        public synchronized Throwable getCause() {
            return super.getCause();
        }

        @Override
        public synchronized Throwable initCause(Throwable cause) {
            return super.initCause(cause);
        }

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        public void printStackTrace() {
            super.printStackTrace();
        }

        @Override
        public void printStackTrace(PrintStream s) {
            super.printStackTrace(s);
        }

        @Override
        public void printStackTrace(PrintWriter s) {
            super.printStackTrace(s);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return super.fillInStackTrace();
        }

        @Override
        public StackTraceElement[] getStackTrace() {
            return super.getStackTrace();
        }

        @Override
        public void setStackTrace(StackTraceElement[] stackTrace) {
            super.setStackTrace(stackTrace);
        }
    }

}
