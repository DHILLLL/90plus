package com.example.app;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.*;


/**
 * GetCoursesInfo @ Get_course_from_jwxt
 * Created by benjaminzhang on 27/09/2017.
 * Modified on 03/10/2017.
 * Version 1.3.7.1710032345
 * Copyright © 2017 benjaminzhang.
 **/
public class GetCoursesInfo {
    private final String[] SERVERADDR = {"210.42.121.134", "210.42.121.133", "210.42.121.132", "210.42.121.241"};
    private int serverIndex = 0;
    private String cookie = null;

    /**
     * @author Ding Zhang
     * @return Cookies
     */
    protected InputStream getGenImg() throws NetworkErrorException {
        InputStream is = null;
        try {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().url("http://" + SERVERADDR[serverIndex] + "/servlet/GenImg").build();
            Response response = client.newCall(request).execute();
            is = response.body().byteStream();
            Headers headers = response.headers();
            List<String> cookies = headers.values("Set-Cookie");
            cookie = cookies.get(0).split(";")[0];
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkErrorException();
        }
        return is;
    }

    /**
     * @author Ding Zhang
     * @param username Username
     * @param password Password
     * @param veriCode Verification Code
     * @param cookie   Cookies (from getGenImg())
     * @return List<Map<String, String>> extractedDataFinal 每一个Map是一个课程的信息，List里的是所有的课程。
     */

    protected List<Map<String, String>> getCourseData(String username, String password, String veriCode, String cookie)
            throws VerificationCodeException, UsernamePasswordErrorException, TimeoutException, IOException {
        List<Map<String, String>> extractedDataFinal = null;
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder().add("id", username).add("pwd", password).add("xdvfb", veriCode).build();
            Request request = new Request.Builder().addHeader("cookie", cookie).url("http://" + SERVERADDR[serverIndex] + "/servlet/Login").post(body).build();
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            if (data.contains("验证码错误")) throw new VerificationCodeException();
            else if (data.contains("用户名/密码错误")) throw new UsernamePasswordErrorException();
            else if (data.contains("会话超时，请重新登陆")) throw new TimeoutException();

            String csrf = data.substring(data.indexOf("csrf"), data.indexOf("csrf") + 46);      // extract csrf code
            System.out.println(csrf);

            Request request2 = new Request.Builder().addHeader("cookie", cookie).url("http://" + SERVERADDR[serverIndex] + "/servlet/Svlt_QueryStuLsn?action=queryStuLsn&" + csrf).build();
            Response response2 = client.newCall(request2).execute();
            String data2 = response2.body().string();
            String[] data3 = data2.split("\\n\\r");
            List<String> extractedRawData = new ArrayList<>();
            for (String i : data3) {
                if (i.contains("lessonName"))   extractedRawData.add(i);
            }
            List<List<String>> extractedData = new ArrayList<>();

            for (String i : extractedRawData){
                List<String> temp = new ArrayList<>();
                String[] extractedRawDataSplited = i.split("\\r\\n");
                for (int j = 0; j < 22; j++){
                    while (extractedRawDataSplited[j].indexOf("\t") != -1){
                        extractedRawDataSplited[j] = extractedRawDataSplited[j].substring(extractedRawDataSplited[j].indexOf("\t") + 1);       // remove useless '\t'
                    }
                    temp.add(extractedRawDataSplited[j]);
                }
                temp.remove(2);         // remove useless weekDay info
                temp.remove(4);         // remove duplicate classNote
                temp.remove(14);        // remove duplicate weekInterVal
                extractedData.add(temp);
            }

            extractedDataFinal = new ArrayList<>();
            for (List<String> i : extractedData) {
                Map<String, String> tempMap = new Hashtable<>();
                for (String j : i) {
                    tempMap.put(j.split("\"")[0].split(" ")[1], j.split("\"")[1]);
                }
                extractedDataFinal.add(tempMap);
            }
        return extractedDataFinal;
    }

    protected String md5(String s) {
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
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
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String getCookie() {
        return cookie;
    }

    class NetworkErrorException extends Exception {
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
    class UsernamePasswordErrorException extends Exception {
        public UsernamePasswordErrorException() {
            super();
        }

        public UsernamePasswordErrorException(String message) {
            super(message);
        }

        public UsernamePasswordErrorException(String message, Throwable cause) {
            super(message, cause);
        }

        public UsernamePasswordErrorException(Throwable cause) {
            super(cause);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public UsernamePasswordErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
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
    class VerificationCodeException extends Exception {
        public VerificationCodeException() {
            super();
        }

        public VerificationCodeException(String message) {
            super(message);
        }

        public VerificationCodeException(String message, Throwable cause) {
            super(message, cause);
        }

        public VerificationCodeException(Throwable cause) {
            super(cause);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public VerificationCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
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
    class TimeoutException extends Exception {
        public TimeoutException() {
            super();
        }

        public TimeoutException(String message) {
            super(message);
        }

        public TimeoutException(String message, Throwable cause) {
            super(message, cause);
        }

        public TimeoutException(Throwable cause) {
            super(cause);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public TimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
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
