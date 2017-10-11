package com.example.app;

import org.litepal.crud.DataSupport;

/**
 * Created by 635901193 on 2017/7/26.
 */

public class Score extends DataSupport{
    private String name;
    private String teacher;
    private Double credit;
    private String type;
    private Double score;
    private Double gpa;
    private String semester;
    private String number;

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public Double getCredit() {
        return credit;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getType() {
        return type;
    }

    public Double getGpa() {
        return gpa;
    }

    public Double getScore() {
        return score;
    }

    public String getNumber() {
        return number;
    }

    public String getSemester() {
        return semester;
    }
}


