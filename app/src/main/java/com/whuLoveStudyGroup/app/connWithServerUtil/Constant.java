/*
 * Copyright (c) 2018 - 2018 benjaminzhang.
 * All rights reserved.
 *
 * Project Name:     90plus
 * File Name:        Constant.java
 * Author:           benjaminzhang
 * Last Modified:    2018/09/03 18:02
 * Version:          0.0.1
 */

package com.whuLoveStudyGroup.app.connWithServerUtil;

public class Constant {
    public static final int SUCCESS = 0;

    public static final int SERVER_INTERNAL_ERROR = 500;
    public static final int DATABASE_QUERY_ERROR = 501;
    public static final int DATABASE_INSERT_ERROR = 502;
    public static final int DATABASE_UPDATE_ERROR = 503;


    public static final int SIGNATURE_ERROR = 400;

    public static final int MESSAGE_MOBILE_NUMBER_ILLEGAL = 40101;
    public static final int MESSAGE_PARAM_LENGTH_LIMIT = 40102;
    public static final int MESSAGE_BUSINESS_LIMIT_CONTROL = 40103;

    public static final int TIMEOUT_ERROR = 402;

    public static final int PARAM_ERROR = 404;
    public static final int PARAM_ERROR_VERIFICATION_CODE_LENGTH = 404011;
    public static final int PARAM_ERROR_VERIFICATION_CODE_ERROR = 404012;
    public static final int PARAM_ERROR_MOBILE_PHONE_LENGTH = 404021;
    public static final int PARAM_ERROR_MOBILE_PHONE_ALREADY_EXIST = 404022;
    public static final int PARAM_ERROR_MOBILE_PHONE_ERROR = 404023;
    public static final int PARAM_ERROR_PASSWORD_LENGTH = 404031;
    public static final int PARAM_ERROR_PASSWORD_STRENGTH = 404032;
    public static final int PARAM_ERROR_PASSWORD_ERROR = 404033;
    public static final int PARAM_ERROR_PASSWORD_LENGTH2 = 404034;
    public static final int PARAM_ERROR_USER_ID_ERROR = 404041;
    public static final int PARAM_ERROR_USER_NOT_EXIST = 404042;
    public static final int PARAM_ERROR_QQ_NUMBER_ERROR = 40405;
    public static final int PARAM_ERROR_USERNAME_LENGTH = 404061;
    public static final int PARAM_ERROR_USERNAME_ERROR = 404062;
    public static final int PARAM_ERROR_ACADEMY_LENGTH = 40407;
    public static final int PARAM_ERROR_PROFESSION_LENGTH = 40408;
    public static final int PARAM_ERROR_SIGNATURE_LENGTH = 40409;
    public static final int PARAM_ERROR_IMEI_LENGTH = 404101;
    public static final int PARAM_ERROR_IMEI_ILLEGAL = 404102;

    public static final int NETWORK_ERROR = 499;    //Client, server has not used


    public static final int UNKNOWN_ERROR = 999;


    public static final String PROTOCOL = "http://";
    public static final String SERVERADDR = "39.108.108.43";
    //    public static final String SERVERADDR = "127.0.0.1";
    public static final String PORT = "9090";
}
