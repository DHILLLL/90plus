/*
 * Copyright (c) 2018 - 2018 benjaminzhang.
 * All rights reserved.
 *
 * Project Name:     90plus
 * File Name:        ConnWithServerQA.java
 * Author:           benjaminzhang
 * Last Modified:    2018/09/03 18:02
 * Version:          0.0.1
 */

package com.whuLoveStudyGroup.app.connWithServerUtil;

public class ConnWithServerQA {
    private Resp resp = null;

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
}
