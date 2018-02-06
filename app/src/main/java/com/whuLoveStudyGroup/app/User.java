package com.whuLoveStudyGroup.app;

/**
 * Created by benjaminzhang on 04/02/2018.
 * Copyright © 2018 benjaminzhang.
 * All rights reserved.
 */

class User {
    private String username = null;
    private String phoneNumber = null;
    private int isSexEqualBoy = -1;
    private int grade = 0;
    private String academy = null;
    private String profession = null;
    private String qqNumber = null;
    private String signature = null;
    private String imageUrl = null;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSex() {
        return isSexEqualBoy;
    }

    public void setSex(int sex) {
        this.isSexEqualBoy = sex;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getQqNumber() {
        return qqNumber;
    }

    public void setQqNumber(String qqNumber) {
        this.qqNumber = qqNumber;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
