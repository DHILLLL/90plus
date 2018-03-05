package com.whuLoveStudyGroup.app;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import okhttp3.*;

import java.io.IOException;
import java.net.ConnectException;
import java.security.MessageDigest;
import java.util.List;

/**
 * Created by benjaminzhang on 28/02/2018.
 * Modified by benjaminzhang on 02/03/2018
 * Copyright © 2018 benjaminzhang.
 * All rights reserved.
 * Version 1.0.1.1803021605
 */

public class ConnWithServerComment {
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
    protected int postComment(int userID, String courseID, String comment) {
        resp = new Resp();

//        final String REQUESTADDR = "90plus/api/v1/add/user/";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String md5ToBe = md5("code=" + code + "&mobile_phone_number=" + phoneNumber + "&password=" + password + "&username=" + username + "&secret_key=sulp09");
//        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("code", String.valueOf(code))
//                .add("username", username)
//                .add("password", password)
//                .add("mobile_phone_number", phoneNumber)
//                .add("md5", md5ToBe).build();
//
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        Call call = okHttpClient.newCall(request);

        try {
//            Response response = call.execute();
//            String respStr = null;
//            if (response.body() != null) {
//                respStr = response.body().string();
//            } else {
//                resp.code = UNKNOWN_ERROR;
//                resp.msg = "UNKNOWN_ERROR";
//                return resp.code;
//            }
            String respStr = "{msg:'',  code:0}";
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, Resp.class);
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
//        } catch (ConnectException e) {
//            resp.code = NETWORK_ERROR;
//            resp.msg = "NETWORK_ERROR";
//        } catch (IOException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        }
        return resp.code;
    }

    /**
     *
     * @return Response data
     */
    protected int postComment2Comment(int userID, int commentID, String comment) {
        resp = new Resp();

//        final String REQUESTADDR = "90plus/api/v1/add/user/";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String md5ToBe = md5("code=" + code + "&mobile_phone_number=" + phoneNumber + "&password=" + password + "&username=" + username + "&secret_key=sulp09");
//        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("code", String.valueOf(code))
//                .add("username", username)
//                .add("password", password)
//                .add("mobile_phone_number", phoneNumber)
//                .add("md5", md5ToBe).build();
//
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        Call call = okHttpClient.newCall(request);

        try {
//            Response response = call.execute();
//            String respStr = null;
//            if (response.body() != null) {
//                respStr = response.body().string();
//            } else {
//                resp.code = UNKNOWN_ERROR;
//                resp.msg = "UNKNOWN_ERROR";
//                return resp.code;
//            }
            String respStr = "{msg:'',  code:0}";
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, Resp.class);
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
//        } catch (ConnectException e) {
//            resp.code = NETWORK_ERROR;
//            resp.msg = "NETWORK_ERROR";
//        } catch (IOException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        }
        return resp.code;
    }

    /**
     *
     * @return Response data
     */
    protected int queryCommentsByCommentID(int commentID) {
        resp = new Resp();

//        final String REQUESTADDR = "90plus/api/v1/add/user/";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String md5ToBe = md5("code=" + code + "&mobile_phone_number=" + phoneNumber + "&password=" + password + "&username=" + username + "&secret_key=sulp09");
//        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("code", String.valueOf(code))
//                .add("username", username)
//                .add("password", password)
//                .add("mobile_phone_number", phoneNumber)
//                .add("md5", md5ToBe).build();
//
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        Call call = okHttpClient.newCall(request);

        try {
//            Response response = call.execute();
//            String respStr = null;
//            if (response.body() != null) {
//                respStr = response.body().string();
//            } else {
//                resp.code = UNKNOWN_ERROR;
//                resp.msg = "UNKNOWN_ERROR";
//                return resp.code;
//            }
            String respStr = "{msg:'', code:0, offset:0, data:{" +
                    "commentID:1, starterUserID:1, starterUsername:'TestTest', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820966, upVoteCount:20, downVoteCount:1, totalCommentsNum:23, upVoted:true, downVoted:false, comment:'Hello World0'}}";
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, new TypeToken<Resp<MyComment>>(){}.getType());
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
//        } catch (ConnectException e) {
//            resp.code = NETWORK_ERROR;
//            resp.msg = "NETWORK_ERROR";
//        } catch (IOException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        }
        return resp.code;
    }

    /**
     *
     * @return Response data
     */
    protected int queryHottestCommentsByCourseID(String courseID) {
        resp = new Resp();

//        final String REQUESTADDR = "90plus/api/v1/add/user/";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String md5ToBe = md5("code=" + code + "&mobile_phone_number=" + phoneNumber + "&password=" + password + "&username=" + username + "&secret_key=sulp09");
//        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("code", String.valueOf(code))
//                .add("username", username)
//                .add("password", password)
//                .add("mobile_phone_number", phoneNumber)
//                .add("md5", md5ToBe).build();
//
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        Call call = okHttpClient.newCall(request);

        try {
//            Response response = call.execute();
//            String respStr = null;
//            if (response.body() != null) {
//                respStr = response.body().string();
//            } else {
//                resp.code = UNKNOWN_ERROR;
//                resp.msg = "UNKNOWN_ERROR";
//                return resp.code;
//            }
            String respStr = "{msg:'', code:0, data:[" +
                    "{commentID:1, starterUserID:1, starterUsername:'TestTest0', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820966, upVoteCount:20, downVoteCount:1, totalCommentsNum:23, upVoted:true, downVoted:false, comment:'Hello World0'}, " +
                    "{commentID:2, starterUserID:1, starterUsername:'TestTest1', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820877, upVoteCount:12, downVoteCount:1, totalCommentsNum:3, upVoted:false, downVoted:true, comment:'Hello World1'}, " +
                    "{commentID:3, starterUserID:1, starterUsername:'TestTest2', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820986, upVoteCount:12, downVoteCount:2, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World2'}, " +
                    "{commentID:4, starterUserID:3, starterUsername:'TestTest3', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820555, upVoteCount:9, downVoteCount:1, totalCommentsNum:2, upVoted:true, downVoted:false, comment:'Hello World3'}, " +
                    "{commentID:5, starterUserID:3, starterUsername:'TestTest4', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820444, upVoteCount:12, downVoteCount:6, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World4'}, " +
                    "{commentID:6, starterUserID:3, starterUsername:'TestTest5', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820333, upVoteCount:6, downVoteCount:1, totalCommentsNum:7, upVoted:true, downVoted:false, comment:'Hello World5'}, " +
                    "{commentID:7, starterUserID:3, starterUsername:'TestTest6', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820932, upVoteCount:5, downVoteCount:1, totalCommentsNum:8, upVoted:true, downVoted:false, comment:'Hello World6'}, " +
                    "{commentID:8, starterUserID:3, starterUsername:'TestTest7', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820944, upVoteCount:4, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World7'}, " +
                    "{commentID:9, starterUserID:3, starterUsername:'TestTest8', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820987, upVoteCount:3, downVoteCount:1, totalCommentsNum:4, upVoted:true, downVoted:false, comment:'Hello World8'}, " +
                    "{commentID:10, starterUserID:3, starterUsername:'TestTest9', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519821000, upVoteCount:2, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World9'}, " +
                    "{commentID:11, starterUserID:1, starterUsername:'TestTest10', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820966, upVoteCount:2, downVoteCount:2, totalCommentsNum:23, upVoted:true, downVoted:false, comment:'Hello World10'}, " +
                    "{commentID:12, starterUserID:1, starterUsername:'TestTest11', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820877, upVoteCount:3, downVoteCount:4, totalCommentsNum:3, upVoted:false, downVoted:true, comment:'Hello World11'}, " +
                    "{commentID:13, starterUserID:1, starterUsername:'TestTest12', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820986, upVoteCount:0, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World12'}, " +
                    "{commentID:14, starterUserID:3, starterUsername:'TestTest13', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820555, upVoteCount:9, downVoteCount:12, totalCommentsNum:2, upVoted:true, downVoted:false, comment:'Hello World13'}, " +
                    "{commentID:15, starterUserID:3, starterUsername:'TestTest14', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820444, upVoteCount:2, downVoteCount:7, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World14'}, " +
                    "{commentID:16, starterUserID:3, starterUsername:'TestTest15', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820333, upVoteCount:3, downVoteCount:9, totalCommentsNum:7, upVoted:true, downVoted:false, comment:'Hello World15'}, " +
                    "{commentID:17, starterUserID:3, starterUsername:'TestTest16', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820932, upVoteCount:0, downVoteCount:9, totalCommentsNum:8, upVoted:true, downVoted:false, comment:'Hello World16'}, " +
                    "{commentID:18, starterUserID:3, starterUsername:'TestTest17', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820944, upVoteCount:1, downVoteCount:12, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World17'}, " +
                    "{commentID:19, starterUserID:3, starterUsername:'TestTest18', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820987, upVoteCount:2, downVoteCount:15, totalCommentsNum:4, upVoted:true, downVoted:false, comment:'Hello World18'}, " +
                    "{commentID:20, starterUserID:3, starterUsername:'TestTest19', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519821000, upVoteCount:2, downVoteCount:17, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World19'}]}";
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, new TypeToken<Resp<List<MyComment>>>(){}.getType());
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
//        } catch (ConnectException e) {
//            resp.code = NETWORK_ERROR;
//            resp.msg = "NETWORK_ERROR";
//        } catch (IOException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        }
        return resp.code;
    }

    /**
     *
     * @return Response data
     */
    protected int queryHottestCommentsByCommentID(int commentID) {
        resp = new Resp();

//        final String REQUESTADDR = "90plus/api/v1/add/user/";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String md5ToBe = md5("code=" + code + "&mobile_phone_number=" + phoneNumber + "&password=" + password + "&username=" + username + "&secret_key=sulp09");
//        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("code", String.valueOf(code))
//                .add("username", username)
//                .add("password", password)
//                .add("mobile_phone_number", phoneNumber)
//                .add("md5", md5ToBe).build();
//
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        Call call = okHttpClient.newCall(request);

        try {
//            Response response = call.execute();
//            String respStr = null;
//            if (response.body() != null) {
//                respStr = response.body().string();
//            } else {
//                resp.code = UNKNOWN_ERROR;
//                resp.msg = "UNKNOWN_ERROR";
//                return resp.code;
//            }
            String respStr = "{msg:'', code:0, data:[" +
                    "{commentID:1, starterUserID:1, starterUsername:'TestTest0', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820966, upVoteCount:20, downVoteCount:1, totalCommentsNum:23, upVoted:true, downVoted:false, comment:'Hello World0'}, " +
                    "{commentID:2, starterUserID:1, starterUsername:'TestTest1', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820877, upVoteCount:12, downVoteCount:1, totalCommentsNum:3, upVoted:false, downVoted:true, comment:'Hello World1'}, " +
                    "{commentID:3, starterUserID:1, starterUsername:'TestTest2', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820986, upVoteCount:12, downVoteCount:2, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World2'}, " +
                    "{commentID:4, starterUserID:3, starterUsername:'TestTest3', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820555, upVoteCount:9, downVoteCount:1, totalCommentsNum:2, upVoted:true, downVoted:false, comment:'Hello World3'}, " +
                    "{commentID:5, starterUserID:3, starterUsername:'TestTest4', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820444, upVoteCount:12, downVoteCount:6, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World4'}, " +
                    "{commentID:6, starterUserID:3, starterUsername:'TestTest5', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820333, upVoteCount:6, downVoteCount:1, totalCommentsNum:7, upVoted:true, downVoted:false, comment:'Hello World5'}, " +
                    "{commentID:7, starterUserID:3, starterUsername:'TestTest6', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820932, upVoteCount:5, downVoteCount:1, totalCommentsNum:8, upVoted:true, downVoted:false, comment:'Hello World6'}, " +
                    "{commentID:8, starterUserID:3, starterUsername:'TestTest7', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820944, upVoteCount:4, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World7'}, " +
                    "{commentID:9, starterUserID:3, starterUsername:'TestTest8', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820987, upVoteCount:3, downVoteCount:1, totalCommentsNum:4, upVoted:true, downVoted:false, comment:'Hello World8'}, " +
                    "{commentID:10, starterUserID:3, starterUsername:'TestTest9', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519821000, upVoteCount:2, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World9'}, " +
                    "{commentID:11, starterUserID:1, starterUsername:'TestTest10', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820966, upVoteCount:2, downVoteCount:2, totalCommentsNum:23, upVoted:true, downVoted:false, comment:'Hello World10'}, " +
                    "{commentID:12, starterUserID:1, starterUsername:'TestTest11', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820877, upVoteCount:3, downVoteCount:4, totalCommentsNum:3, upVoted:false, downVoted:true, comment:'Hello World11'}, " +
                    "{commentID:13, starterUserID:1, starterUsername:'TestTest12', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820986, upVoteCount:0, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World12'}, " +
                    "{commentID:14, starterUserID:3, starterUsername:'TestTest13', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820555, upVoteCount:9, downVoteCount:12, totalCommentsNum:2, upVoted:true, downVoted:false, comment:'Hello World13'}, " +
                    "{commentID:15, starterUserID:3, starterUsername:'TestTest14', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820444, upVoteCount:2, downVoteCount:7, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World14'}, " +
                    "{commentID:16, starterUserID:3, starterUsername:'TestTest15', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820333, upVoteCount:3, downVoteCount:9, totalCommentsNum:7, upVoted:true, downVoted:false, comment:'Hello World15'}, " +
                    "{commentID:17, starterUserID:3, starterUsername:'TestTest16', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820932, upVoteCount:0, downVoteCount:9, totalCommentsNum:8, upVoted:true, downVoted:false, comment:'Hello World16'}, " +
                    "{commentID:18, starterUserID:3, starterUsername:'TestTest17', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820944, upVoteCount:1, downVoteCount:12, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World17'}, " +
                    "{commentID:19, starterUserID:3, starterUsername:'TestTest18', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820987, upVoteCount:2, downVoteCount:15, totalCommentsNum:4, upVoted:true, downVoted:false, comment:'Hello World18'}, " +
                    "{commentID:20, starterUserID:3, starterUsername:'TestTest19', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519821000, upVoteCount:2, downVoteCount:17, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World19'}]}";
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, new TypeToken<Resp<List<MyComment>>>(){}.getType());
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
//        } catch (ConnectException e) {
//            resp.code = NETWORK_ERROR;
//            resp.msg = "NETWORK_ERROR";
//        } catch (IOException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        }
        return resp.code;
    }

    /**
     *
     * @return Response data
     */
    protected int queryNewestCommentsByCourseID(String courseID, int offset) {
        resp = new Resp();

//        final String REQUESTADDR = "90plus/api/v1/add/user/";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String md5ToBe = md5("code=" + code + "&mobile_phone_number=" + phoneNumber + "&password=" + password + "&username=" + username + "&secret_key=sulp09");
//        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("code", String.valueOf(code))
//                .add("username", username)
//                .add("password", password)
//                .add("mobile_phone_number", phoneNumber)
//                .add("md5", md5ToBe).build();
//
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        Call call = okHttpClient.newCall(request);

        try {
//            Response response = call.execute();
//            String respStr = null;
//            if (response.body() != null) {
//                respStr = response.body().string();
//            } else {
//                resp.code = UNKNOWN_ERROR;
//                resp.msg = "UNKNOWN_ERROR";
//                return resp.code;
//            }
            String respStr = "{msg:'', code:0, ";
            if (offset < 20)    respStr += "offset:20, ";
            else respStr += "offset:" + (offset + 20) + ", ";
            respStr += "data:[" +
                    "{commentID:1, starterUserID:1, starterUsername:'TestTest0', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820966, upVoteCount:20, downVoteCount:1, totalCommentsNum:23, upVoted:true, downVoted:false, comment:'Hello World0'}, " +
                    "{commentID:2, starterUserID:1, starterUsername:'TestTest1', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820877, upVoteCount:12, downVoteCount:1, totalCommentsNum:3, upVoted:false, downVoted:true, comment:'Hello World1'}, " +
                    "{commentID:3, starterUserID:1, starterUsername:'TestTest2', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820986, upVoteCount:12, downVoteCount:2, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World2'}, " +
                    "{commentID:4, starterUserID:3, starterUsername:'TestTest3', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820555, upVoteCount:9, downVoteCount:1, totalCommentsNum:2, upVoted:true, downVoted:false, comment:'Hello World3'}, " +
                    "{commentID:5, starterUserID:3, starterUsername:'TestTest4', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820444, upVoteCount:12, downVoteCount:6, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World4'}, " +
                    "{commentID:6, starterUserID:3, starterUsername:'TestTest5', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820333, upVoteCount:6, downVoteCount:1, totalCommentsNum:7, upVoted:true, downVoted:false, comment:'Hello World5'}, " +
                    "{commentID:7, starterUserID:3, starterUsername:'TestTest6', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820932, upVoteCount:5, downVoteCount:1, totalCommentsNum:8, upVoted:true, downVoted:false, comment:'Hello World6'}, " +
                    "{commentID:8, starterUserID:3, starterUsername:'TestTest7', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820944, upVoteCount:4, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World7'}, " +
                    "{commentID:9, starterUserID:3, starterUsername:'TestTest8', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820987, upVoteCount:3, downVoteCount:1, totalCommentsNum:4, upVoted:true, downVoted:false, comment:'Hello World8'}, " +
                    "{commentID:10, starterUserID:3, starterUsername:'TestTest9', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519821000, upVoteCount:2, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World9'}, " +
                    "{commentID:11, starterUserID:1, starterUsername:'TestTest10', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519810966, upVoteCount:2, downVoteCount:2, totalCommentsNum:23, upVoted:true, downVoted:false, comment:'Hello World10'}, " +
                    "{commentID:12, starterUserID:1, starterUsername:'TestTest11', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519810877, upVoteCount:3, downVoteCount:4, totalCommentsNum:3, upVoted:false, downVoted:true, comment:'Hello World11'}, " +
                    "{commentID:13, starterUserID:1, starterUsername:'TestTest12', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519810986, upVoteCount:0, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World12'}, " +
                    "{commentID:14, starterUserID:3, starterUsername:'TestTest13', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810555, upVoteCount:9, downVoteCount:12, totalCommentsNum:2, upVoted:true, downVoted:false, comment:'Hello World13'}, " +
                    "{commentID:15, starterUserID:3, starterUsername:'TestTest14', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810444, upVoteCount:2, downVoteCount:7, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World14'}, " +
                    "{commentID:16, starterUserID:3, starterUsername:'TestTest15', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810333, upVoteCount:3, downVoteCount:9, totalCommentsNum:7, upVoted:true, downVoted:false, comment:'Hello World15'}, " +
                    "{commentID:17, starterUserID:3, starterUsername:'TestTest16', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810932, upVoteCount:0, downVoteCount:9, totalCommentsNum:8, upVoted:true, downVoted:false, comment:'Hello World16'}, " +
                    "{commentID:18, starterUserID:3, starterUsername:'TestTest17', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810944, upVoteCount:1, downVoteCount:12, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World17'}, " +
                    "{commentID:19, starterUserID:3, starterUsername:'TestTest18', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810987, upVoteCount:2, downVoteCount:15, totalCommentsNum:4, upVoted:true, downVoted:false, comment:'Hello World18'}, " +
                    "{commentID:20, starterUserID:3, starterUsername:'TestTest19', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519811000, upVoteCount:2, downVoteCount:17, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World19'}]}";
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, new TypeToken<Resp<List<MyComment>>>(){}.getType());
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
//        } catch (ConnectException e) {
//            resp.code = NETWORK_ERROR;
//            resp.msg = "NETWORK_ERROR";
//        } catch (IOException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        }
        return resp.code;
    }

    /**
     *
     * @return Response data
     */
    protected int queryNewestCommentsByCommentID(int commentID, int offset) {
        resp = new Resp();

//        final String REQUESTADDR = "90plus/api/v1/add/user/";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String md5ToBe = md5("code=" + code + "&mobile_phone_number=" + phoneNumber + "&password=" + password + "&username=" + username + "&secret_key=sulp09");
//        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("code", String.valueOf(code))
//                .add("username", username)
//                .add("password", password)
//                .add("mobile_phone_number", phoneNumber)
//                .add("md5", md5ToBe).build();
//
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        Call call = okHttpClient.newCall(request);

        try {
//            Response response = call.execute();
//            String respStr = null;
//            if (response.body() != null) {
//                respStr = response.body().string();
//            } else {
//                resp.code = UNKNOWN_ERROR;
//                resp.msg = "UNKNOWN_ERROR";
//                return resp.code;
//            }
            String respStr = "{msg:'', code:0, ";
            if (offset < 10)    respStr += "offset:20, ";
            else respStr += "offset:" + (offset + 20) + ", ";
            respStr += "data:[" +
                    "{commentID:1, starterUserID:1, starterUsername:'TestTest0', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820966, upVoteCount:20, downVoteCount:1, totalCommentsNum:23, upVoted:true, downVoted:false, comment:'Hello World0'}, " +
                    "{commentID:2, starterUserID:1, starterUsername:'TestTest1', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820877, upVoteCount:12, downVoteCount:1, totalCommentsNum:3, upVoted:false, downVoted:true, comment:'Hello World1'}, " +
                    "{commentID:3, starterUserID:1, starterUsername:'TestTest2', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519820986, upVoteCount:12, downVoteCount:2, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World2'}, " +
                    "{commentID:4, starterUserID:3, starterUsername:'TestTest3', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820555, upVoteCount:9, downVoteCount:1, totalCommentsNum:2, upVoted:true, downVoted:false, comment:'Hello World3'}, " +
                    "{commentID:5, starterUserID:3, starterUsername:'TestTest4', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820444, upVoteCount:12, downVoteCount:6, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World4'}, " +
                    "{commentID:6, starterUserID:3, starterUsername:'TestTest5', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820333, upVoteCount:6, downVoteCount:1, totalCommentsNum:7, upVoted:true, downVoted:false, comment:'Hello World5'}, " +
                    "{commentID:7, starterUserID:3, starterUsername:'TestTest6', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820932, upVoteCount:5, downVoteCount:1, totalCommentsNum:8, upVoted:true, downVoted:false, comment:'Hello World6'}, " +
                    "{commentID:8, starterUserID:3, starterUsername:'TestTest7', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820944, upVoteCount:4, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World7'}, " +
                    "{commentID:9, starterUserID:3, starterUsername:'TestTest8', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519820987, upVoteCount:3, downVoteCount:1, totalCommentsNum:4, upVoted:true, downVoted:false, comment:'Hello World8'}, " +
                    "{commentID:10, starterUserID:3, starterUsername:'TestTest9', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519821000, upVoteCount:2, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World9'}, " +
                    "{commentID:11, starterUserID:1, starterUsername:'TestTest10', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519810966, upVoteCount:2, downVoteCount:2, totalCommentsNum:23, upVoted:true, downVoted:false, comment:'Hello World10'}, " +
                    "{commentID:12, starterUserID:1, starterUsername:'TestTest11', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519810877, upVoteCount:3, downVoteCount:4, totalCommentsNum:3, upVoted:false, downVoted:true, comment:'Hello World11'}, " +
                    "{commentID:13, starterUserID:1, starterUsername:'TestTest12', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_1_avatar_0kApKG1.jpg', unixTime:1519810986, upVoteCount:0, downVoteCount:1, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World12'}, " +
                    "{commentID:14, starterUserID:3, starterUsername:'TestTest13', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810555, upVoteCount:9, downVoteCount:12, totalCommentsNum:2, upVoted:true, downVoted:false, comment:'Hello World13'}, " +
                    "{commentID:15, starterUserID:3, starterUsername:'TestTest14', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810444, upVoteCount:2, downVoteCount:7, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World14'}, " +
                    "{commentID:16, starterUserID:3, starterUsername:'TestTest15', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810333, upVoteCount:3, downVoteCount:9, totalCommentsNum:7, upVoted:true, downVoted:false, comment:'Hello World15'}, " +
                    "{commentID:17, starterUserID:3, starterUsername:'TestTest16', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810932, upVoteCount:0, downVoteCount:9, totalCommentsNum:8, upVoted:true, downVoted:false, comment:'Hello World16'}, " +
                    "{commentID:18, starterUserID:3, starterUsername:'TestTest17', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810944, upVoteCount:1, downVoteCount:12, totalCommentsNum:2, upVoted:false, downVoted:true, comment:'Hello World17'}, " +
                    "{commentID:19, starterUserID:3, starterUsername:'TestTest18', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519810987, upVoteCount:2, downVoteCount:15, totalCommentsNum:4, upVoted:true, downVoted:false, comment:'Hello World18'}, " +
                    "{commentID:20, starterUserID:3, starterUsername:'TestTest19', starterUserImageThumbnailUrl:'http://39.108.108.43:9090/media/user_image/user_3_avatar.jpg', unixTime:1519811000, upVoteCount:2, downVoteCount:17, totalCommentsNum:2, upVoted:false, downVoted:false, comment:'Hello World19'}]}";
            Gson gson = new Gson();
            resp = gson.fromJson(respStr, new TypeToken<Resp<List<MyComment>>>(){}.getType());
        } catch (JsonSyntaxException e) {
            resp.code = UNKNOWN_ERROR;
            resp.msg = "UNKNOWN_ERROR";
        }
//        } catch (ConnectException e) {
//            resp.code = NETWORK_ERROR;
//            resp.msg = "NETWORK_ERROR";
//        } catch (IOException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        }
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
     *
     * @return Response offset
     */
    protected int getResponseOffset() {
        return resp.offset;
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
        private int offset = 0;
        private T data = null;
    }


//    public static void main(String[] args) {
//        ConnWithServerComment conn = new ConnWithServerComment();
////        int x = conn.postComment(1,"201212","lalala");
////        int x = conn.postComment2Comment(1,12312,"lalala");
////        int x = conn.queryCommentsByCommentID(2);
////        int x = conn.queryHottestCommentsByCourseID("123");
////        int x = conn.queryHottestCommentsByCommentID(123);
////        int x = conn.queryNewestCommentsByCommentID(12,0);
//        int x = conn.queryNewestCommentsByCommentID(12, 20);
////        int x = conn.queryNewestCommentsByCommentID(12, 0);
////        int x = conn.queryNewestCommentsByCommentID(12, 30);
//        System.out.println(x);
//        System.out.println(conn.getResponseData());
//        System.out.println(conn.getResponseMsg());
//        System.out.println(conn.getResponseOffset());
//    }

}


//new TypeToken<Resp<MyComment[]>>(){}.getType()
//     */
//    protected int getVerificationCode(String code, String phoneNumber) {
//        resp = new Resp();
//        if (code.length() < 6) {
//            resp.code = PARAM_ERROR_VERIFICATION_CODE_LENGTH;
//            resp.msg = "PARAM_ERROR_VERIFICATION_CODE_LENGTH";
//            return resp.code;
//        }
//        if (phoneNumber.length() != 11) {
//            resp.code = PARAM_ERROR_MOBILE_PHONE_LENGTH;
//            resp.msg = "PARAM_ERROR_MOBILE_PHONE_LENGTH";
//            return resp.code;
//        }
//
//        final String REQUESTADDR = "90plus/api/v1/get/verification_code/";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String req_time = Long.toString(System.currentTimeMillis());
//        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR + req_time + "/" + phoneNumber + "/" + code + "/" +
//                md5("code=" + code + "&receiver=" + phoneNumber + "&req_time=" + req_time + "&secret_key=sulp09") + "/";
//
//        Request request = new Request.Builder().url(url).build();
//        Call call = okHttpClient.newCall(request);
//        try {
//            Response response = call.execute();
//            String respStr = null;
//            if (response.body() != null) {
//                respStr = response.body().string();
//            } else {
//                resp.code = UNKNOWN_ERROR;
//                resp.msg = "UNKNOWN_ERROR";
//                return resp.code;
//            }
//            Gson gson = new Gson();
//            resp = gson.fromJson(respStr, Resp.class);
//        } catch (JsonSyntaxException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        } catch (ConnectException e) {
//            resp.code = NETWORK_ERROR;
//            resp.msg = "NETWORK_ERROR";
//        } catch (IOException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        }
//        return resp.code;
//    }
//
//    protected int addUser(String code, String phoneNumber, String username, String password) {
//        resp = new Resp();
//        if (code.length() < 6) {
//            resp.code = PARAM_ERROR_VERIFICATION_CODE_LENGTH;
//            resp.msg = "PARAM_ERROR_VERIFICATION_CODE_LENGTH";
//            return resp.code;
//        }
//        if (phoneNumber.length() != 11) {
//            resp.code = PARAM_ERROR_MOBILE_PHONE_LENGTH;
//            resp.msg = "PARAM_ERROR_MOBILE_PHONE_LENGTH";
//            return resp.code;
//        }
//        if (username.length() > 15) {
//            resp.code = PARAM_ERROR_USERNAME_LENGTH;
//            resp.msg = "PARAM_ERROR_USERNAME_LENGTH";
//            return resp.code;
//        }
//        if (password.length() < 8) {
//            resp.code = PARAM_ERROR_PASSWORD_LENGTH;
//            resp.msg = "PARAM_ERROR_PASSWORD_LENGTH";
//            return resp.code;
//        } else if (password.length() > 25) {
//            resp.code = PARAM_ERROR_PASSWORD_LENGTH2;
//            resp.msg = "PARAM_ERROR_PASSWORD_LENGTH2";
//            return resp.code;
//        } else {
//            if (password.matches("^\\d+$") || password.matches("^[a-zA-Z]+$")) {
//                resp.code = PARAM_ERROR_PASSWORD_STRENGTH;
//                resp.msg = "PARAM_ERROR_PASSWORD_STRENGTH";
//                return resp.code;
//            }
//        }
//
//        final String REQUESTADDR = "90plus/api/v1/add/user/";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String md5ToBe = md5("code=" + code + "&mobile_phone_number=" + phoneNumber + "&password=" + password + "&username=" + username + "&secret_key=sulp09");
//        String url = PROTOCOL + SERVERADDR + ":" + PORT + "/" + REQUESTADDR;
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("code", String.valueOf(code))
//                .add("username", username)
//                .add("password", password)
//                .add("mobile_phone_number", phoneNumber)
//                .add("md5", md5ToBe).build();
//
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        Call call = okHttpClient.newCall(request);
//
//        try {
//            Response response = call.execute();
//            String respStr = null;
//            if (response.body() != null) {
//                respStr = response.body().string();
//            } else {
//                resp.code = UNKNOWN_ERROR;
//                resp.msg = "UNKNOWN_ERROR";
//                return resp.code;
//            }
//            Gson gson = new Gson();
//            resp = gson.fromJson(respStr, Resp.class);
//        } catch (JsonSyntaxException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        } catch (ConnectException e) {
//            resp.code = NETWORK_ERROR;
//            resp.msg = "NETWORK_ERROR";
//        } catch (IOException e) {
//            resp.code = UNKNOWN_ERROR;
//            resp.msg = "UNKNOWN_ERROR";
//        }
//        return resp.code;
//    }
