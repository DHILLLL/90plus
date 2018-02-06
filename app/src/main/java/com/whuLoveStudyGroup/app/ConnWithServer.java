package com.whuLoveStudyGroup.app;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.security.MessageDigest;

/**
 * Created by benjaminzhang on 29/01/2018.
 * Modified by benjaminzhang on 04/02/2018
 * Copyright © 2018 benjaminzhang.
 * All rights reserved.
 * Version 1.2.3.1802041720
 */

public class ConnWithServer {
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
     * @param code          Verification code
     * @param phoneNumber   Target mobile phone number
     * @return              Response status
     * @throws PARAM_ERROR_VERIFICATION_CODE_LENGTH 404011  Verification code length too short
     * @throws PARAM_ERROR_MOBILE_PHONE_LENGTH      404021  Mobile phone number length error
     * @throws SIGNATURE_ERROR                      400     Signature error
     * @throws MESSAGE_MOBILE_NUMBER_ILLEGAL        40101   Mobile phone number illegal
     * @throws MESSAGE_PARAM_LENGTH_LIMIT           40102   Verification code length error
     * @throws MESSAGE_BUSINESS_LIMIT_CONTROL       40103   Send verification code too often
     * @throws TIMEOUT_ERROR                        402     Timeout
     * @throws NETWORK_ERROR                        499     Network disconnected or bad connection or timeout
     * @throws UNKNOWN_ERROR                        999     Unknown
     */
    protected int getVerificationCode(String code, String phoneNumber) {
        resp = new Resp();
        if (code.length() < 6) {
            resp.code = PARAM_ERROR_VERIFICATION_CODE_LENGTH;
            resp.msg = "PARAM_ERROR_VERIFICATION_CODE_LENGTH";
            return resp.code;
        }
        if (phoneNumber.length() != 11) {
            resp.code = PARAM_ERROR_MOBILE_PHONE_LENGTH;
            resp.msg = "PARAM_ERROR_MOBILE_PHONE_LENGTH";
            return resp.code;
        }

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
            } else {
                resp.code = UNKNOWN_ERROR;
                resp.msg = "UNKNOWN_ERROR";
                return resp.code;
            }
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, Resp.class);
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        } catch (ConnectException e) {
            resp.code = NETWORK_ERROR;
            resp.msg = "NETWORK_ERROR";
        } catch (IOException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
        return resp.code;
    }

    /**
     * 调用完该方法后记得调用getResponseData获取userID信息。        !!!
     * 在获取userID之前请不要调用其他方法，否则将无法再次获得userID。  !!!
     *
     * @param code          Verification Code
     * @param phoneNumber   Mobile phone number
     * @param username      Username
     * @param password      Password
     * @return              Response status
     * @throws PARAM_ERROR_VERIFICATION_CODE_LENGTH     404011  Verification code length too short
     * @throws PARAM_ERROR_MOBILE_PHONE_LENGTH          404021  Mobile phone number length error
     * @throws PARAM_ERROR_USERNAME_LENGTH              404061  Username length too long
     * @throws PARAM_ERROR_PASSWORD_LENGTH              404031  Password length too short
     * @throws PARAM_ERROR_PASSWORD_STRENGTH            404032  Password weak
     * @throws PARAM_ERROR_PASSWORD_LENGTH2             404034  Password length too long
     * @throws PARAM_ERROR_VERIFICATION_CODE_ERROR      404012  Verification code error
     * @throws PARAM_ERROR_MOBILE_PHONE_ALREADY_EXIST   404022  Mobile phone number existed
     * @throws PARAM_ERROR                              404     Parameters error
     * @throws SIGNATURE_ERROR                          400     Signature error
     * @throws NETWORK_ERROR                            499     Network disconnected or bad connection or timeout
     * @throws UNKNOWN_ERROR                            999     Unknown
     */
    protected int addUser(String code, String phoneNumber, String username, String password) {
        resp = new Resp();
        if (code.length() < 6) {
            resp.code = PARAM_ERROR_VERIFICATION_CODE_LENGTH;
            resp.msg = "PARAM_ERROR_VERIFICATION_CODE_LENGTH";
            return resp.code;
        }
        if (phoneNumber.length() != 11) {
            resp.code = PARAM_ERROR_MOBILE_PHONE_LENGTH;
            resp.msg = "PARAM_ERROR_MOBILE_PHONE_LENGTH";
            return resp.code;
        }
        if (username.length() > 15) {
            resp.code = PARAM_ERROR_USERNAME_LENGTH;
            resp.msg = "PARAM_ERROR_USERNAME_LENGTH";
            return resp.code;
        }
        if (password.length() < 8) {
            resp.code = PARAM_ERROR_PASSWORD_LENGTH;
            resp.msg = "PARAM_ERROR_PASSWORD_LENGTH";
            return resp.code;
        } else if (password.length() > 25) {
            resp.code = PARAM_ERROR_PASSWORD_LENGTH2;
            resp.msg = "PARAM_ERROR_PASSWORD_LENGTH2";
            return resp.code;
        } else {
            if (password.matches("^\\d+$") || password.matches("^[a-zA-Z]+$")) {
                resp.code = PARAM_ERROR_PASSWORD_STRENGTH;
                resp.msg = "PARAM_ERROR_PASSWORD_STRENGTH";
                return resp.code;
            }
        }

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
            } else {
                resp.code = UNKNOWN_ERROR;
                resp.msg = "UNKNOWN_ERROR";
                return resp.code;
            }
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, Resp.class);
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        } catch (ConnectException e) {
            resp.code = NETWORK_ERROR;
            resp.msg = "NETWORK_ERROR";
        } catch (IOException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
        return resp.code;
    }

    /**
     * 不用修改的项String/File 填null，int 填-1  !!!
     *
     * 除了phoneNumber以外的所有参数都可以不填。写成一个方法的原因是统一方法，不需要写多个方法。
     *
     * @param academy               学院
     * @param isPhoneNumberPublic   是否公开电话号码
     * @param grade                 年级
     * @param phoneNumber           Mobile phone number
     * @param profession            专业
     * @param qqNum                 QQ number
     * @param sex                   Sex (男则为1，女则为0)
     * @param signature             个性签名
     * @param username              Username
     * @param userImage             用户头像
     * @return                      Response status
     * @throws PARAM_ERROR_ACADEMY_LENGTH               40407   User academy length too long
     * @throws PARAM_ERROR_MOBILE_PHONE_LENGTH          404021  Mobile phone number length error
     * @throws PARAM_ERROR_USERNAME_LENGTH              404061  Username length too long
     * @throws PARAM_ERROR_PROFESSION_LENGTH            40408   User profession length too long
     * @throws PARAM_ERROR_SIGNATURE_LENGTH             40409   User signature length too long
     * @throws PARAM_ERROR_QQ_NUMBER_ERROR              40405   QQ number error
     * @throws PARAM_ERROR                              404     Parameters error
     * @throws SIGNATURE_ERROR                          400     Signature error
     * @throws NETWORK_ERROR                            499     Network disconnected or bad connection or timeout
     * @throws UNKNOWN_ERROR                            999     Unknown
     */
    protected int editUser(String academy, int isPhoneNumberPublic, int grade, String phoneNumber,
                           String profession, String qqNum, int sex, String signature, String username, File userImage) {
        resp = new Resp();
        final String REQUESTADDR = "90plus/api/v1/edit/user/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;

        MultipartBody.Builder requestBodyPostBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (userImage != null)
            requestBodyPostBuilder.addFormDataPart("user_image", userImage.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), userImage));

        String toBeMd5 = "academy=";
        if (academy != null) {
            if (academy.length() > 18) {
                resp.code = PARAM_ERROR_ACADEMY_LENGTH;
                resp.msg = "PARAM_ERROR_ACADEMY_LENGTH";
                return resp.code;
            }
            requestBodyPostBuilder.addFormDataPart("academy", academy);
            toBeMd5 += academy;
        }

        toBeMd5 += "&is_mobile_phone_number_public=";
        if (isPhoneNumberPublic == 0) {
            requestBodyPostBuilder.addFormDataPart("is_mobile_phone_number_public", String.valueOf(false));
            toBeMd5 += "false";
        }
        else if (isPhoneNumberPublic == 1) {
            requestBodyPostBuilder.addFormDataPart("is_mobile_phone_number_public", String.valueOf(true));
            toBeMd5 += "true";
        }

        toBeMd5 += "&grade=";
        if (grade > 2010) {
            requestBodyPostBuilder.addFormDataPart("grade", String.valueOf(grade));
            toBeMd5 += grade;
        }

        toBeMd5 += "&mobile_phone_number=";
        if (phoneNumber != null) {
            if (phoneNumber.length() != 11) {
                resp.code = PARAM_ERROR_MOBILE_PHONE_LENGTH;
                resp.msg = "PARAM_ERROR_MOBILE_PHONE_LENGTH";
                return resp.code;
            }
            requestBodyPostBuilder.addFormDataPart("mobile_phone_number", phoneNumber);
            toBeMd5 += phoneNumber;
        }

        toBeMd5 += "&profession=";
        if (profession != null) {
            if (profession.length() > 22) {
                resp.code = PARAM_ERROR_PROFESSION_LENGTH;
                resp.msg = "PARAM_ERROR_PROFESSION_LENGTH";
                return resp.code;
            }
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
        } else if (sex == -1) {
            requestBodyPostBuilder.addFormDataPart("sex", "none");
            toBeMd5 += "none";
        }

        toBeMd5 += "&signature=";
        if (signature != null) {
            if (signature.length() > 200) {
                resp.code = PARAM_ERROR_SIGNATURE_LENGTH;
                resp.msg = "PARAM_ERROR_SIGNATURE_LENGTH";
                return resp.code;
            }
            requestBodyPostBuilder.addFormDataPart("signature", signature);
            toBeMd5 += signature;
        }

        toBeMd5 += "&username=";
        if (username != null) {
            if (username.length() > 15) {
                resp.code = PARAM_ERROR_USERNAME_LENGTH;
                resp.msg = "PARAM_ERROR_USERNAME_LENGTH";
                return resp.code;
            }
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
            } else {
                resp.code = UNKNOWN_ERROR;
                resp.msg = "UNKNOWN_ERROR";
                return resp.code;
            }
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, new TypeToken<Resp<User>>(){}.getType());
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        } catch (ConnectException e) {
            resp.code = NETWORK_ERROR;
            resp.msg = "NETWORK_ERROR";
        } catch (IOException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
        return resp.code;
    }

    /**
     *
     * @param code          Verification Code
     * @param phoneNumber   Mobile phone number
     * @param password      New password
     * @return              Response status
     * @throws PARAM_ERROR_VERIFICATION_CODE_LENGTH     404011  Verification code length too short
     * @throws PARAM_ERROR_VERIFICATION_CODE_ERROR      404012  Verification code error
     * @throws PARAM_ERROR_MOBILE_PHONE_LENGTH          404021  Mobile phone number length error
     * @throws PARAM_ERROR_PASSWORD_LENGTH              404031  Password length too short
     * @throws PARAM_ERROR_PASSWORD_STRENGTH            404032  Password weak
     * @throws PARAM_ERROR_PASSWORD_LENGTH2             404034  Password length too long
     * @throws PARAM_ERROR_USER_NOT_EXIST               404042  Mobile number hasn't benn registered
     * @throws PARAM_ERROR                              404     Parameters error
     * @throws SIGNATURE_ERROR                          400     Signature error
     * @throws NETWORK_ERROR                            499     Network disconnected or bad connection or timeout
     * @throws UNKNOWN_ERROR                            999     Unknown
     */
    protected int changePassword(String code, String phoneNumber, String password) {
        resp = new Resp();
        if (code.length() < 6) {
            resp.code = PARAM_ERROR_VERIFICATION_CODE_LENGTH;
            resp.msg = "PARAM_ERROR_VERIFICATION_CODE_LENGTH";
            return resp.code;
        }
        if (phoneNumber.length() != 11) {
            resp.code = PARAM_ERROR_MOBILE_PHONE_LENGTH;
            resp.msg = "PARAM_ERROR_MOBILE_PHONE_LENGTH";
            return resp.code;
        }
        if (password.length() < 8) {
            resp.code = PARAM_ERROR_PASSWORD_LENGTH;
            resp.msg = "PARAM_ERROR_PASSWORD_LENGTH";
            return resp.code;
        } else if (password.length() > 25) {
            resp.code = PARAM_ERROR_PASSWORD_LENGTH2;
            resp.msg = "PARAM_ERROR_PASSWORD_LENGTH2";
            return resp.code;
        } else {
            if (password.matches("^\\d+$") || password.matches("^[a-zA-Z]+$")) {
                resp.code = PARAM_ERROR_PASSWORD_STRENGTH;
                resp.msg = "PARAM_ERROR_PASSWORD_STRENGTH";
                return resp.code;
            }
        }

        final String REQUESTADDR = "90plus/api/v1/edit/user/password/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String md5ToBe = md5("code=" + code + "&mobile_phone_number=" + phoneNumber + "&new_password=" + password + "&secret_key=sulp09");
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;

        RequestBody requestBody = new FormBody.Builder()
                .add("code", String.valueOf(code))
                .add("new_password", password)
                .add("mobile_phone_number", phoneNumber)
                .add("md5", md5ToBe).build();

        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            String respStr = null;
            if (response.body() != null) {
                respStr = response.body().string();
            } else {
                resp.code = UNKNOWN_ERROR;
                resp.msg = "UNKNOWN_ERROR";
                return resp.code;
            }
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, Resp.class);
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        } catch (ConnectException e) {
            resp.code = NETWORK_ERROR;
            resp.msg = "NETWORK_ERROR";
        } catch (IOException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
        return resp.code;
    }

    /**
     *
     * @param code              Verification Code
     * @param oldPhoneNumber    Old mobile phone number
     * @param newPhoneNumber    New mobile phone number
     * @return                  Response status
     * @throws PARAM_ERROR_VERIFICATION_CODE_LENGTH     404011  Verification code length too short
     * @throws PARAM_ERROR_VERIFICATION_CODE_ERROR      404012  Verification code error
     * @throws PARAM_ERROR_MOBILE_PHONE_LENGTH          404021  Mobile phone number length error
     * @throws PARAM_ERROR_USER_NOT_EXIST               404042  Mobile number hasn't benn registered
     * @throws PARAM_ERROR                              404     Parameters error
     * @throws SIGNATURE_ERROR                          400     Signature error
     * @throws NETWORK_ERROR                            499     Network disconnected or bad connection or timeout
     * @throws UNKNOWN_ERROR                            999     Unknown
     */
    protected int changePhoneNumber(String code, String oldPhoneNumber, String newPhoneNumber) {
        resp = new Resp();
        if (code.length() < 6) {
            resp.code = PARAM_ERROR_VERIFICATION_CODE_LENGTH;
            resp.msg = "PARAM_ERROR_VERIFICATION_CODE_LENGTH";
            return resp.code;
        }
        if (oldPhoneNumber.length() != 11 && newPhoneNumber.length() != 11) {
            resp.code = PARAM_ERROR_MOBILE_PHONE_LENGTH;
            resp.msg = "PARAM_ERROR_MOBILE_PHONE_LENGTH";
            return resp.code;
        }

        final String REQUESTADDR = "90plus/api/v1/edit/user/change/mobile_phone_number/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String req_time = Long.toString(System.currentTimeMillis());
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR + oldPhoneNumber + "/" + newPhoneNumber + "/" + code + "/" +
                md5("code=" + code + "&new_phone=" + newPhoneNumber + "&old_phone=" + oldPhoneNumber + "&secret_key=sulp09") + "/";

        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            String respStr = null;
            if (response.body() != null) {
                respStr = response.body().string();
            } else {
                resp.code = UNKNOWN_ERROR;
                resp.msg = "UNKNOWN_ERROR";
                return resp.code;
            }
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, Resp.class);
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        } catch (ConnectException e) {
            resp.code = NETWORK_ERROR;
            resp.msg = "NETWORK_ERROR";
        } catch (IOException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
        return resp.code;
    }


    /**
     *
     * @param phoneNumber   Mobile phone number
     * @return              Response status
     * @throws PARAM_ERROR_MOBILE_PHONE_LENGTH          404021  Mobile phone number length error
     * @throws PARAM_ERROR_USER_NOT_EXIST               404042  User not exist
     * @throws SIGNATURE_ERROR                          400     Signature error
     * @throws NETWORK_ERROR                            499     Network disconnected or bad connection or timeout
     * @throws UNKNOWN_ERROR                            999     Unknown
     */
    protected int getUserImageAddr(String phoneNumber) {
        resp = new Resp();
        if (phoneNumber.length() != 11) {
            resp.code = PARAM_ERROR_MOBILE_PHONE_LENGTH;
            resp.msg = "PARAM_ERROR_MOBILE_PHONE_LENGTH";
            return resp.code;
        }

        final String REQUESTADDR = "90plus/api/v1/get/user/image/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR + phoneNumber + "/" +
                md5("mobile_phone_number=" + phoneNumber + "&secret_key=sulp09") + "/";

        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String respStr = null;
            if (response.body() != null) {
                respStr = response.body().string();
            } else {
                resp.code = UNKNOWN_ERROR;
                resp.msg = "UNKNOWN_ERROR";
                return resp.code;
            }
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, Resp.class);
            if (resp.data != null && !resp.data.equals(""))
                resp.data = PROTOCOL + SERVERADDR + ":" + PORT + resp.data;
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        } catch (ConnectException e) {
            resp.code = NETWORK_ERROR;
            resp.msg = "NETWORK_ERROR";
        } catch (IOException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
        return resp.code;
    }

    /**
     *
     * @param phoneNumber   Mobile phone number
     * @param password      Password
     * @return              Response status
     * @throws PARAM_ERROR_MOBILE_PHONE_LENGTH          404021  Mobile phone number length error
     * @throws PARAM_ERROR_PASSWORD_LENGTH              404031  Password length too short
     * @throws PARAM_ERROR_PASSWORD_LENGTH2             404034  Password length too long
     * @throws PARAM_ERROR_PASSWORD_ERROR               404033  Password incorrect
     * @throws PARAM_ERROR_MOBILE_PHONE_ERROR           404023  Mobile phone not in users list
     * @throws PARAM_ERROR                              404     Parameters error
     * @throws SIGNATURE_ERROR                          400     Signature error
     * @throws NETWORK_ERROR                            499     Network disconnected or bad connection or timeout
     * @throws UNKNOWN_ERROR                            999     Unknown
     */
    protected int loginUser(String phoneNumber, String password) {
        resp = new Resp();
        if (phoneNumber.length() != 11) {
            resp.code = PARAM_ERROR_MOBILE_PHONE_LENGTH;
            resp.msg = "PARAM_ERROR_MOBILE_PHONE_LENGTH";
            return resp.code;
        }
        if (password.length() < 8) {
            resp.code = PARAM_ERROR_PASSWORD_LENGTH;
            resp.msg = "PARAM_ERROR_PASSWORD_LENGTH";
            return resp.code;
        } else if (password.length() > 25) {
            resp.code = PARAM_ERROR_PASSWORD_LENGTH2;
            resp.msg = "PARAM_ERROR_PASSWORD_LENGTH2";
            return resp.code;
        }

        final String REQUESTADDR = "90plus/api/v1/get/user/login/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String md5ToBe = md5("mobile_phone_number=" + phoneNumber + "&password=" + password + "&secret_key=sulp09");
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;

        RequestBody requestBody = new FormBody.Builder()
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
            } else {
                resp.code = UNKNOWN_ERROR;
                resp.msg = "UNKNOWN_ERROR";
                return resp.code;
            }
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, new TypeToken<Resp<User>>(){}.getType());


        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        } catch (ConnectException e) {
            resp.code = NETWORK_ERROR;
            resp.msg = "NETWORK_ERROR";
        } catch (IOException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
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
    private class Resp<T> {
        private String msg = null;
        private int code = 0;
        private T data = null;
    }
}
