package com.whuLoveStudyGroup.app;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by benjaminzhang on 16/10/2017.
 * Copyright © 2017 benjaminzhang.
 * All rights reserved.
 */

class GetVersionInfoFromDB {
    private final String URL = "jdbc:mysql://39.108.108.43:3306/90Plus?useSSL=false";
    private final String NAME = "90plus_client";
    private final String PASSWORD = "Tyj8RfbIv+lGF/IrhMjf7agSieJDdxrA7vGPN26Tw20=";
    private Connection conn = null;
    private List<Map> queryResult = new ArrayList<>();

    private void connMySQLDatabase() throws UnknownErrorException, NetworkErrorException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("未能成功加载驱动程序，请检查是否导入驱动程序！");
            //添加一个println，如果加载驱动异常，检查是否添加驱动，或者添加驱动字符串是否错误
            e.printStackTrace();
            throw new UnknownErrorException();
        }
        try {
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println("获取数据库连接成功！");
        } catch (SQLException e) {
            System.out.println("获取数据库连接失败！");
            //添加一个println，如果连接失败，检查连接字符串或者登录名以及密码是否错误
            e.printStackTrace();
            throw new NetworkErrorException();
        }
    }


    private void retrieveVersionInfo() throws UnknownErrorException, NetworkErrorException{
        Statement stmt = null; //创建Statement对象
        try {
            stmt = conn.createStatement();
            String sql = "select * from software_info";    //要执行的SQL
            ResultSet rs = stmt.executeQuery(sql);//创建数据对象
            try {
                while (rs.next()) {
                    Map<String, String> tempMap = new HashMap<>();
                    tempMap.put("versionID", rs.getString(1));
                    tempMap.put("version", rs.getString(2));
                    tempMap.put("info", rs.getString(3));
                    tempMap.put("changeLog", rs.getString(4));
                    tempMap.put("critical", rs.getString(5));
                    tempMap.put("fileSize", rs.getString(6));
                    tempMap.put("downloadAddress", rs.getString(7));
                    queryResult.add(tempMap);
                }
                rs.close();
            }
            catch (SQLException e) {
                throw new UnknownErrorException();
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NetworkErrorException();
        }
    }

    protected void connAndGetVersionInfo() throws UnknownErrorException, NetworkErrorException{
        connMySQLDatabase();
        retrieveVersionInfo();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                conn = null;
            }
        }
    }

    protected int getLatestVersionID() {
        return Integer.parseInt(queryResult.get(queryResult.size() - 1).get("versionID").toString());
    }

    protected String getLatestVersion() {
        return queryResult.get(queryResult.size() - 1).get("version").toString();
    }

    protected String getLatestInfo() {
        return queryResult.get(queryResult.size() - 1).get("info").toString();
    }

    protected String getLatestChangeLog() {
        return queryResult.get(queryResult.size() - 1).get("changeLog").toString();
    }

    protected boolean isLatestCritical() {
        return (queryResult.get(queryResult.size() - 1).get("critical").toString().equals("1")) ? true : false;
    }

    protected long getLatestFileSize() {
        return Long.parseLong(queryResult.get(queryResult.size() - 1).get("fileSize").toString());
    }

    protected String getLatestDownloadAddress() {
        return queryResult.get(queryResult.size() - 1).get("downloadAddress").toString();
    }

    protected List<UpdateInformation> getUpdateHistory() {
        List<UpdateInformation> list = new ArrayList<>();
        for (int i = queryResult.size() - 1; i >= 0; i--) {
            UpdateInformation temp = new UpdateInformation(queryResult.get(i).get("version").toString(), queryResult.get(i).get("changeLog").toString());
            list.add(temp);
        }
        return list;
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
