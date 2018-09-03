/*
 * Copyright (c) 2018 - 2018 benjaminzhang.
 * All rights reserved.
 *
 * Project Name:     90plus
 * File Name:        MyComment.java
 * Author:           benjaminzhang
 * Last Modified:    2018/09/03 18:02
 * Version:          0.0.1
 */

package com.whuLoveStudyGroup.app.connWithServerUtil;

/**
 * Created by 635901193 on 2017/7/24.
 */

public class MyComment {
    private int commentID = -999;
    private int starterUserID = -999;
    private String starterUsername = null;
    private String starterUserImageThumbnailUrl = null;
    private int unixTime = -999;
    private int upVoteCount = 0;
    private int downVoteCount = 0;
    private int totalCommentsNum = 0;
    private boolean upVoted = false;
    private boolean downVoted = false;
    private String comment = null;

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getStarterUserID() {
        return starterUserID;
    }

    public void setStarterUserID(int starterUserID) {
        this.starterUserID = starterUserID;
    }

    public String getStarterUsername() {
        return starterUsername;
    }

    public void setStarterUsername(String starterUsername) {
        this.starterUsername = starterUsername;
    }

    public String getStarterUserImageThumbnailUrl() {
        return starterUserImageThumbnailUrl;
    }

    public void setStarterUserImageThumbnailUrl(String starterUserImageThumbnailUrl) {
        this.starterUserImageThumbnailUrl = starterUserImageThumbnailUrl;
    }

    public int getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(int unixTime) {
        this.unixTime = unixTime;
    }

    public int getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(int upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public int getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(int downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public int getTotalCommentsNum() {
        return totalCommentsNum;
    }

    public void setTotalCommentsNum(int totalCommentsNum) {
        this.totalCommentsNum = totalCommentsNum;
    }

    public boolean isUpVoted() {
        return upVoted;
    }

    public void setUpVoted(boolean upVoted) {
        this.upVoted = upVoted;
    }

    public boolean isDownVoted() {
        return downVoted;
    }

    public void setDownVoted(boolean downVoted) {
        this.downVoted = downVoted;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
