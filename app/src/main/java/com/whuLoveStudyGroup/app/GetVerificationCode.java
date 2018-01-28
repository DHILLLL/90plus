package com.whuLoveStudyGroup.app;

/**
 * Created by benjaminzhang on 28/01/2018.
 * Modified by benjaminzhang on 28/01/2018.
 * Copyright © 2018 benjaminzhang.
 * All rights reserved.
 * Version 1.0.1.1801282200
 */

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.*;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.security.MessageDigest;

public class GetVerificationCode {
    private final String PROTOCOL = "http://";
    private final String SERVERADDR = "39.108.108.43";
//    private final String SERVERADDR = "127.0.0.1";
    private final String PORT = "9090";
    private final String REQUESTADDR = "90plus/api/v1/get/verification_code";
    private Resp resp = null;

    protected int getVerificationCode(int code, String phoneNUmber) throws UnknownErrorException, NetworkErrorException {
        OkHttpClient okHttpClient = new OkHttpClient();
        String req_time = Long.toString(System.currentTimeMillis());
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR + "/" + req_time + "/" + phoneNUmber + "/" + code + "/" +
                md5("code=" + code + "&receiver=" + phoneNUmber + "&req_time=" + req_time + "&secret_key=sulp09");
//        System.out.println(url);
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
        return resp.code;
    }

    protected String getResponceMsg() {
        return resp.msg;
    }

    /**
     * @param s String which needs to be encrypted
     * @author Ding Zhang
     */
    private String md5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class Resp {
        private String msg = null;
        private int code = 0;
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
