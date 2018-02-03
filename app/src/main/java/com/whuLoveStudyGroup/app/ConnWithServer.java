package com.whuLoveStudyGroup.app;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.security.MessageDigest;

/**
 * Created by benjaminzhang on 29/01/2018.
 * Modified by benjaminzhang on 03/02/2018
 * Copyright © 2018 benjaminzhang.
 * All rights reserved.
 * Version 1.2.1.1802031720
 */

public class ConnWithServer {
    private final String PROTOCOL = "http://";
    private final String SERVERADDR = "39.108.108.43";
//    private final String SERVERADDR = "127.0.0.1";
    private final String PORT = "9090";
    private Resp resp = null;


    /**
     *
     * @param code                      Verification code
     * @param phoneNumber               Target mobile phone number
     * @return Response status
     * @throws UnknownErrorException    Unknown error or server error
     * @throws NetworkErrorException    Network connection failed or error
     */
    protected int getVerificationCode(String code, String phoneNumber) throws UnknownErrorException, NetworkErrorException {
        resp = null;
        final String REQUESTADDR = "90plus/api/v1/get/verification_code/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String req_time = Long.toString(System.currentTimeMillis());
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR + req_time + "/" + phoneNumber + "/" + code + "/" +
                md5("code=" + code + "&receiver=" + phoneNumber + "&req_time=" + req_time + "&secret_key=sulp09") + "/";

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

    /**
     * 调用完该方法后记得调用getResponseData获取userID信息。        !!!
     * 在获取userID之前请不要调用其他方法，否则将无法再次获得userID。  !!!
     *
     * @param code                      Verification Code
     * @param phoneNumber               Mobile phone number
     * @param username                  Username
     * @param password                  Password
     * @return Response status
     * @throws UnknownErrorException    Unknown error or server error
     * @throws NetworkErrorException    Network connection failed or error
     */
    protected int addUser(String code, String phoneNumber, String username, String password) throws UnknownErrorException, NetworkErrorException {
        resp = null;
        final String REQUESTADDR = "90plus/api/v1/add/user/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String md5ToBe = md5("code=" + code + "&mobile_phone_number=" + phoneNumber + "&password=" + password + "&username=" + username + "&secret_key=sulp09");
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;

        RequestBody requestBody = new FormBody.Builder()
                .add("code", String.valueOf(code))
                .add("username", username)
                .add("password", password)
                .add("mobile_phone_number", phoneNumber)
                .add("md5", md5ToBe).build();

        Request request = new Request.Builder().url(url).post(requestBody).build();
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

    /**
     * 不用修改的项String/File 填null，int 填-1  !!!
     *
     * 除了userID以外的所有参数都可以不填。写成一个方法的原因是统一方法，不需要写多个方法。
     *
     * @param academy                   学院
     * @param code                      Verification code
     * @param grade                     年级
     * @param phoneNumber               Mobile phone number
     * @param password                  Password
     * @param profession                专业
     * @param qqNum                     QQ number
     * @param sex                       Sex (男则为1，女则为0，否则为-1)
     * @param signature                 个性签名
     * @param userID                    User unique id
     * @param username                  Username
     * @param userImage                 用户头像
     * @return Response status
     * @throws UnknownErrorException    Unknown error or server error
     * @throws NetworkErrorException    Network connection failed or error
     */
    protected int editUser(String academy, String code, int grade, String phoneNumber, String password, String profession, String qqNum,
                           int sex, String signature, int userID, String username, File userImage) throws UnknownErrorException, NetworkErrorException {
        resp = null;
        final String REQUESTADDR = "90plus/api/v1/edit/user/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;

        MultipartBody.Builder requestBodyPostBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (userImage != null)
            requestBodyPostBuilder.addFormDataPart("user_image", userImage.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), userImage));

        String toBeMd5 = "academy=";
        if (academy != null) {
            requestBodyPostBuilder.addFormDataPart("academy", academy);
            toBeMd5 += academy;
        }

        toBeMd5 += "&code=";
        if (code != null) {
            requestBodyPostBuilder.addFormDataPart("code", code);
            toBeMd5 += code;
        }

        toBeMd5 += "&grade=";
        if (grade > 2010) {
            requestBodyPostBuilder.addFormDataPart("grade", String.valueOf(grade));
            toBeMd5 += grade;
        }

        toBeMd5 += "&mobile_phone_number=";
        if (phoneNumber != null) {
            requestBodyPostBuilder.addFormDataPart("mobile_phone_number", phoneNumber);
            toBeMd5 += phoneNumber;
        }

        toBeMd5 += "&password=";
        if (password != null) {
            requestBodyPostBuilder.addFormDataPart("password", password);
            toBeMd5 += password;
        }

        toBeMd5 += "&profession=";
        if (profession != null) {
            requestBodyPostBuilder.addFormDataPart("profession", profession);
            toBeMd5 += profession;
        }

        toBeMd5 += "&qq_number=";
        if (qqNum != null) {
            requestBodyPostBuilder.addFormDataPart("qq_number", qqNum);
            toBeMd5 += qqNum;
        }

        toBeMd5 += "&sex=";
        if (sex == 0) {
            requestBodyPostBuilder.addFormDataPart("sex", String.valueOf(false));
            toBeMd5 += "false";
        }
        else if (sex == 1) {
            requestBodyPostBuilder.addFormDataPart("sex", String.valueOf(true));
            toBeMd5 += "true";
        }

        toBeMd5 += "&signature=";
        if (signature != null) {
            requestBodyPostBuilder.addFormDataPart("signature", signature);
            toBeMd5 += signature;
        }

        toBeMd5 += "&user_id=";
        if (userID > 0) {
            requestBodyPostBuilder.addFormDataPart("user_id", String.valueOf(userID));
            toBeMd5 += userID;
        }

        toBeMd5 += "&username=";
        if (username != null) {
            requestBodyPostBuilder.addFormDataPart("username", username);
            toBeMd5 += username;
        }

        toBeMd5 += "&secret_key=sulp09";
        requestBodyPostBuilder.addFormDataPart("md5", md5(toBeMd5));


        Request request = new Request.Builder().url(url).post(requestBodyPostBuilder.build()).build();
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
            e.printStackTrace();
            throw new UnknownErrorException();
        }
        return resp.code;
    }

    /**
     *
     * @param userID                    User unique id
     * @param phoneNumber               Mobile phone number
     * @return Response status
     * @throws UnknownErrorException    Unknown error or server error
     * @throws NetworkErrorException    Network connection failed or error
     */
    protected int getUserImageAddr(int userID, String phoneNumber) throws UnknownErrorException, NetworkErrorException {
        resp = null;
        final String REQUESTADDR = "90plus/api/v1/get/user/image/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR + userID + "/" + phoneNumber + "/" +
                md5("mobile_phone_number=" + phoneNumber + "&user_id=" + userID + "&secret_key=sulp09") + "/";

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
            resp.data = PROTOCOL + SERVERADDR + ":" + PORT + "/media/" + resp.data;
        } catch (JsonSyntaxException e) {
            throw new UnknownErrorException();
        } catch (ConnectException e) {
            throw new NetworkErrorException();
        } catch (IOException e) {
            throw new UnknownErrorException();
        }
        return resp.code;
    }

    /**
     *
     * @return Response data
     */
    protected Object getResponseData() {
        return resp.data;
    }

    /**
     *
     * @return Response message
     */
    protected String getResponseMsg() {
        return resp.msg;
    }

    /**
     * @param s String which needs to be encrypted
     * @return  encrypted string
     */
    protected String md5(String s) {
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

    // Parse response json
    private class Resp {
        private String msg = null;
        private int code = 0;
        private Object data = null;
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
