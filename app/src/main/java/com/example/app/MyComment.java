package com.example.app;

/**
 * Created by 635901193 on 2017/7/24.
 */

public class MyComment {

    private int image;
    private String username;
    private String time;
    private String words;
    private int up;
    private int down;

    public MyComment(int image, String username, String time, String words, int up, int down){
        this.image = image;
        this.username = username;
        this.time = time;
        this.words = words;
        this.up = up;
        this.down = down;
    }

    public int getImage(){
        return image;
    }

    public String getUsername(){
        return username;
    }

    public int getDown() {
        return down;
    }

    public int getUp() {
        return up;
    }

    public String getTime() {
        return time;
    }

    public String getWords() {
        return words;
    }
}
