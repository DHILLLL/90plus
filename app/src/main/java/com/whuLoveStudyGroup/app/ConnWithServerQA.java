package com.whuLoveStudyGroup.app;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.security.MessageDigest;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by benjaminzhang on 28/02/2018.
 * Modified by benjaminzhang on 28/02/2018
 * Copyright © 2018 benjaminzhang.
 * All rights reserved.
 * Version 1.0.0.1802281800
 */

public class ConnWithServerQA {
    private final int SUCCESS = 0;

    private final int SERVER_INTERNAL_ERROR = 500;
    private final int DATABASE_QUERY_ERROR = 501;
    private final int DATABASE_INSERT_ERROR = 502;
    private final int DATABASE_UPDATE_ERROR = 503;


    private final int SIGNATURE_ERROR = 400;

    private final int MESSAGE_MOBILE_NUMBER_ILLEGAL = 40101;
    private final int MESSAGE_PARAM_LENGTH_LIMIT = 40102;
    private final int MESSAGE_BUSINESS_LIMIT_CONTROL = 40103;

    private final int TIMEOUT_ERROR = 402;

    private final int PARAM_ERROR = 404;
    private final int PARAM_ERROR_VERIFICATION_CODE_LENGTH = 404011;
    private final int PARAM_ERROR_VERIFICATION_CODE_ERROR = 404012;
    private final int PARAM_ERROR_MOBILE_PHONE_LENGTH = 404021;
    private final int PARAM_ERROR_MOBILE_PHONE_ALREADY_EXIST = 404022;
    private final int PARAM_ERROR_MOBILE_PHONE_ERROR = 404023;
    private final int PARAM_ERROR_PASSWORD_LENGTH = 404031;
    private final int PARAM_ERROR_PASSWORD_STRENGTH = 404032;
    private final int PARAM_ERROR_PASSWORD_ERROR = 404033;
    private final int PARAM_ERROR_PASSWORD_LENGTH2 = 404034;
    private final int PARAM_ERROR_USER_ID_ERROR = 404041;
    private final int PARAM_ERROR_USER_NOT_EXIST = 404042;
    private final int PARAM_ERROR_QQ_NUMBER_ERROR = 40405;
    private final int PARAM_ERROR_USERNAME_LENGTH = 404061;
    private final int PARAM_ERROR_USERNAME_ERROR = 404062;
    private final int PARAM_ERROR_ACADEMY_LENGTH = 40407;
    private final int PARAM_ERROR_PROFESSION_LENGTH = 40408;
    private final int PARAM_ERROR_SIGNATURE_LENGTH = 40409;

    private final int NETWORK_ERROR = 499;    //Client, server has not used


    private final int UNKNOWN_ERROR = 999;


    private final String PROTOCOL = "http://";
    private final String SERVERADDR = "39.108.108.43";
    //    private final String SERVERADDR = "127.0.0.1";
    private final String PORT = "9090";
    private Resp resp = null;




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

    // Parse response json
    private class Resp<T> {
        private String msg = null;
        private int code = 0;
        private T data = null;
    }
}
