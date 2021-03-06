/*
 * Copyright (c) 2018 - 2018 benjaminzhang.
 * All rights reserved.
 *
 * Project Name:     90plus
 * File Name:        User.java
 * Author:           benjaminzhang
 * Last Modified:    2018/09/03 18:02
 * Version:          0.0.1
 */

package com.whuLoveStudyGroup.app.connWithServerUtil;

public class User {
    private int userID = -999;
    private String username = null;
    private String phoneNumber = null;
    private int isSexEqualBoy = -1;
    private int grade = 0;
    private String academy = null;
    private String profession = null;
    private String qqNumber = null;
    private String signature = null;
    private String imageUrl = null;
    private String imageThumbnailUrl = null;
    private int isPhoneNumberPublic = 0;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

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

    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;
    }

    public void setImageThumbnailUrl(String imageThumbnailUrl) {
        this.imageThumbnailUrl = imageThumbnailUrl;
    }

    public int getIsPhoneNumberPublic() {
        return isPhoneNumberPublic;
    }

    public void setIsPhoneNumberPublic(int phoneNumberPublic) {
        isPhoneNumberPublic = phoneNumberPublic;
    }
}
