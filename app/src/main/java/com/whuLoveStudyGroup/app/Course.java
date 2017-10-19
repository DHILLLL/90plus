package com.whuLoveStudyGroup.app;

import org.litepal.crud.DataSupport;

/**
 * Created by 635901193 on 2017/7/26.
 */

public class Course extends DataSupport{
    private String name;
    private boolean everyWeek;//是否每周上课
    private int weekFrom;
    private int weekTo;
    private int timesPerWeek;
    private int weekday;
    private int hourFrom;
    private int hourTo;
    private String place;
    private int color;
    private String teacher;
    private String credit;
    private String type;
    private String note;//课程笔记
    private boolean homework;//是否有未完成作业

    public void setHomework(boolean homework) {
        this.homework = homework;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public void setHourFrom(int hourFrom) {
        this.hourFrom = hourFrom;
    }

    public void setHourTo(int hourTo) {
        this.hourTo = hourTo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setTimesPerWeek(int timesPerWeek) {
        this.timesPerWeek = timesPerWeek;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeekFrom(int weekFrom) {
        this.weekFrom = weekFrom;
    }

    public void setWeekTo(int weekTo) {
        this.weekTo = weekTo;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setEveryWeek(boolean everyWeek){
        this.everyWeek = everyWeek;
    }

    public int getTimesPerWeek() {
        return timesPerWeek;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getCredit() {
        return credit;
    }

    public int getWeekday() {
        return weekday;
    }

    public int getHourFrom() {
        return hourFrom;
    }

    public int getHourTo() {
        return hourTo;
    }

    public int getWeekFrom() {
        return weekFrom;
    }

    public int getWeekTo() {
        return weekTo;
    }

    public String getNote() {
        return note;
    }

    public String getPlace() {
        return place;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getType() {
        return type;
    }

    public boolean getEveryWeek(){
        return everyWeek;
    }

    public boolean getHomework(){
        return homework;
    }



}


