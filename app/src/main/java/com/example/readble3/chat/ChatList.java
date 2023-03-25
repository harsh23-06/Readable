package com.example.readble3.chat;

public class ChatList {

    private final String mobile, message, date, time;
    float speed;

    public ChatList(String mobile, String message, String date, String time) {
//        this.speed = speed;
        this.mobile = mobile;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public float getSpeed() {
        return speed;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
