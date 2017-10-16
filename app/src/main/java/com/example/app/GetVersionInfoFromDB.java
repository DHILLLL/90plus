package com.example.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.*;

/**
 * Created by benjaminzhang on 16/10/2017.
 * Copyright © 2017 benjaminzhang.
 * All rights reserved.
 */

class GetVersionInfoFromDB {
    private final String URL = "jdbc:mysql://39.108.108.43:3306/90Plus";
    private final String NAME = "90plus_client";
    private final String PASSWORD = "Tyj8RfbIv+lGF/IrhMjf7agSieJDdxrA7vGPN26Tw20=";
    private Connection conn = null;
    private int versionID = 0;
    private String version = null;
    private String info = null;
    private String changeLog = null;
    private boolean critical = false;
    private double fileSize = 0.0;
    private String downloadAddress = null;

    private void connMySQLDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("未能成功加载驱动程序，请检查是否导入驱动程序！");
            //添加一个println，如果加载驱动异常，检查是否添加驱动，或者添加驱动字符串是否错误
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println("获取数据库连接成功！");
        } catch (SQLException e) {
            System.out.println("获取数据库连接失败！");
            //添加一个println，如果连接失败，检查连接字符串或者登录名以及密码是否错误
            e.printStackTrace();
        }
    }


    private void retrieveVersionInfo() {
        Statement stmt = null; //创建Statement对象
        try {
            stmt = conn.createStatement();
            String sql = "select * from software_info";    //要执行的SQL
            ResultSet rs = stmt.executeQuery(sql);//创建数据对象
            while (rs.next()) {
                versionID = rs.getInt(1);
                version = rs.getString(2);
                info = rs.getString(3);
                changeLog = rs.getString(4);
                critical = rs.getBoolean(5);
                fileSize = rs.getDouble(6);
                downloadAddress = rs.getString(7);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void connAndGetVersionInfo() {
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

    protected int getVersionID() {
        return versionID;
    }

    protected String getVersion() {
        return version;
    }

    protected String getInfo() {
        return info;
    }

    protected String getChangeLog() {
        return changeLog;
    }

    protected boolean isCritical() {
        return critical;
    }

    protected double getFileSize() {
        return fileSize;
    }

    protected String getDownloadAddress() {
        return downloadAddress;
    }
}
