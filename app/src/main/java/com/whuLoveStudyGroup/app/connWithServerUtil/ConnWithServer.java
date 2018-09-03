/*
 * Copyright (c) 2018 - 2018 benjaminzhang.
 * All rights reserved.
 *
 * Project Name:     90plus
 * File Name:        ConnWithServer.java
 * Author:           benjaminzhang
 * Last Modified:    2018/09/03 18:05
 * Version:          0.0.1
 */

package com.whuLoveStudyGroup.app.connWithServerUtil;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.MESSAGE_BUSINESS_LIMIT_CONTROL;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.MESSAGE_MOBILE_NUMBER_ILLEGAL;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.MESSAGE_PARAM_LENGTH_LIMIT;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.NETWORK_ERROR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_ACADEMY_LENGTH;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_IMEI_ILLEGAL;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_IMEI_LENGTH;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_MOBILE_PHONE_ALREADY_EXIST;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_MOBILE_PHONE_ERROR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_MOBILE_PHONE_LENGTH;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_PASSWORD_ERROR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_PASSWORD_LENGTH;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_PASSWORD_LENGTH2;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_PASSWORD_STRENGTH;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_PROFESSION_LENGTH;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_QQ_NUMBER_ERROR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_SIGNATURE_LENGTH;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_USERNAME_LENGTH;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_USER_NOT_EXIST;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_VERIFICATION_CODE_ERROR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PARAM_ERROR_VERIFICATION_CODE_LENGTH;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PORT;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.PROTOCOL;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.SERVERADDR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.SERVER_INTERNAL_ERROR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.SIGNATURE_ERROR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.TIMEOUT_ERROR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.Constant.UNKNOWN_ERROR;
import static com.whuLoveStudyGroup.app.connWithServerUtil.MD5Util.md5;

public class ConnWithServer {
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
    public int getVerificationCode(String code, String phoneNumber) {
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
    public int addUser(String code, String phoneNumber, String username, String password) {
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
     * @param userImageThumbnail    用户头像缩略图
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
    public int editUser(String academy, int isPhoneNumberPublic, int grade, String phoneNumber,
                           String profession, String qqNum, int sex, String signature, String username,
                           File userImage, File userImageThumbnail) {
        resp = new Resp();
        final String REQUESTADDR = "90plus/api/v1/edit/user/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;

        MultipartBody.Builder requestBodyPostBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (userImage != null)
            requestBodyPostBuilder.addFormDataPart("user_image", userImage.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), userImage));

        if (userImageThumbnail != null)
            requestBodyPostBuilder.addFormDataPart("user_image_thumbnail", userImageThumbnail.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), userImageThumbnail));

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
    public int changePassword(String code, String phoneNumber, String password) {
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
    public int changePhoneNumber(String code, String oldPhoneNumber, String newPhoneNumber) {
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
    public int getUserImageAddr(String phoneNumber) {
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
    public int getUserImageThumbnailAddr(String phoneNumber) {
        resp = new Resp();
        if (phoneNumber.length() != 11) {
            resp.code = PARAM_ERROR_MOBILE_PHONE_LENGTH;
            resp.msg = "PARAM_ERROR_MOBILE_PHONE_LENGTH";
            return resp.code;
        }

        final String REQUESTADDR = "90plus/api/v1/get/user/image_thumbnail/";
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
    public int loginUser(String phoneNumber, String password) {
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
     * @param IMEI   IMEI (length = 15)
     * @return       Response status
     * @throws PARAM_ERROR_IMEI_LENGTH                  404101  IMEI length error
     * @throws PARAM_ERROR_IMEI_ILLEGAL                 404102  IMEI error
     * @throws SERVER_INTERNAL_ERROR                    500     Server Internal Error
     * @throws SIGNATURE_ERROR                          400     Signature error
     * @throws NETWORK_ERROR                            499     Network disconnected or bad connection or timeout
     * @throws UNKNOWN_ERROR                            999     Unknown
     */
    public int recordInstallation(String IMEI) {
        resp = new Resp();
        if (IMEI.length() == 15) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(IMEI);
            if(!isNum.matches()){
                resp.code = PARAM_ERROR_IMEI_ILLEGAL;
                resp.msg = "PARAM_ERROR_IMEI_ILLEGAL";
                return resp.code;
            }
        } else {
            resp.code = PARAM_ERROR_IMEI_LENGTH;
            resp.msg = "PARAM_ERROR_IMEI_LENGTH";
            return resp.code;
        }

        final String REQUESTADDR = "90plus/api/v1/add/userInstall/";
        OkHttpClient okHttpClient = new OkHttpClient();
        String md5ToBe = md5("imei=" + IMEI + "&secret_key=sulp09");
        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;

        RequestBody requestBody = new FormBody.Builder()
                .add("imei", IMEI)
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
            resp = gson.fromJson(respStr, new TypeToken<Resp<String>>(){}.getType());


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
    public Object getResponseData() {
        return resp.data;
    }

    /**
     *
     * @return Response message
     */
    public String getResponseMsg() {
        return resp.msg;
    }

    // Parse response json
    private class Resp<T> {
        private String msg = null;
        private int code = 0;
        private T data = null;
    }

    public static void main(String[] args) {
        ConnWithServer x = new ConnWithServer();
        System.out.println(x.recordInstallation("123456789012345"));
        System.out.println(x.getResponseMsg());
        System.out.println(x.getResponseData());
    }
}
