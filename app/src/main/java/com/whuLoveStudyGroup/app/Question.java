package com.whuLoveStudyGroup.app;

/**
 * Created by 635901193 on 2017/7/24.
 */

public class Question {

    private int image;
    private String username;
    private String time;
    private String words;
    private int answer;

    public Question(int image, String username, String time, String words, int answer){
        this.image = image;
        this.username = username;
        this.time = time;
        this.words = words;
        this.answer = answer;
    }

    public int getImage(){
        return image;
    }

    public String getUsername(){
        return username;
    }

    public int getAnswer() { return answer; }

    public String getTime() {
        return time;
    }

    public String getWords() {
        return words;
    }
}
