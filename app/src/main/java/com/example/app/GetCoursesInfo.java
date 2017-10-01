package com.example.app;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import okhttp3.*;


/**
 * GetCoursesInfo @ Get_course_from_jwxt
 * Created by benjaminzhang on 27/09/2017.
 * Copyright © 2017 benjaminzhang.
 **/
public class GetCoursesInfo {
    /**
     * @author Ding Zhang
     * @return Cookies
     */
    protected String getGenImg() {
        String cookiePurified = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://210.42.121.134/servlet/GenImg").build();
            Response response = client.newCall(request).execute();
            DataInputStream is = new DataInputStream(response.body().byteStream());

            // FIXME    The following block of code needs to be changed when developing on an Android environment
            File file = new File("GenImg.jpeg");
            FileOutputStream fop = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            try {
                while (true) {
                    fop.write(is.readByte());
                }
            } catch (EOFException e) {
                //System.out.println("EOF");
            }
            fop.flush();
            fop.close();
            // END FIXME

            Headers headers = response.headers();
            List<String> cookies = headers.values("Set-Cookie");
            String cookie = cookies.get(0);
            cookiePurified = cookie.substring(0, cookie.indexOf(";"));
            // System.out.println(cookie);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return cookiePurified;
    }

    /**
     * @author Ding Zhang
     * @param username Username
     * @param password Password
     * @param veriCode Verification Code
     * @param cookie   Cookies (from getGenImg())
     * @return List<Map<String, String>> extractedDataFinal 每一个Map是一个课程的信息，List里的是所有的课程。
     */

    protected List<Map<String, String>> getCourseData(String username, String password, String veriCode, String cookie) {
        List<Map<String, String>> extractedDataFinal = null;
        try {
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder().add("id", username).add("pwd", password).add("xdvfb", veriCode).build();
            Request request = new Request.Builder().addHeader("cookie", cookie).url("http://210.42.121.134/servlet/Login").post(body).build();
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            String csrf = data.substring(data.indexOf("csrf"), data.indexOf("csrf") + 46);      // extract csrf code
            System.out.println(csrf);

            Request request2 = new Request.Builder().addHeader("cookie", cookie).url("http://210.42.121.134/servlet/Svlt_QueryStuLsn?action=queryStuLsn&" + csrf).build();
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
        }
        catch (Exception e) {
            e.printStackTrace();
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
}
