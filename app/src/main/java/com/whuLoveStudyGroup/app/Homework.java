package com.whuLoveStudyGroup.app;

import org.litepal.crud.DataSupport;

/**
 * Created by 635901193 on 2017/7/20.
 */

public class Homework extends DataSupport {
    private String course;
    private String word;//具体作业的文字
    private String pic;
    private long time;//创建时间，作为主键
    private String deadLine;
    private boolean isPhoto;//此处笔误，应为hasPhoto，是否有图片
    private boolean finished;

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setIsPhoto(boolean photo) {
        isPhoto = photo;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getTime() {
        return time;
    }

    public String getCourse(){
        return course;
    }

    public String getPic() {
        return pic;
    }

    public String getWord() {
        return word;
    }

    public boolean getIsPhoto() {
        return isPhoto;
    }

    public boolean getFinished() { return finished; }

    public String getDeadLine() {
        return deadLine;
    }
}
