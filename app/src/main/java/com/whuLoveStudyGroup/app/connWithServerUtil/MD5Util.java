/*
 * Copyright (c) 2018 - 2018 benjaminzhang.
 * All rights reserved.
 *
 * Project Name:     90plus
 * File Name:        MD5Util.java
 * Author:           benjaminzhang
 * Last Modified:    2018/09/03 18:23
 * Version:          0.0.1
 */

package com.whuLoveStudyGroup.app.connWithServerUtil;

import java.security.MessageDigest;

public class MD5Util {
    /**
     * @param s String which needs to be encrypted
     * @return  encrypted string
     */
    public static String md5(String s) {
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
}
