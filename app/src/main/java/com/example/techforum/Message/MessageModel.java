package com.example.techforum.Message;

public class MessageModel {
    public String uid,message;
    public String time;



    public String getUid() {
        return uid;
    }

    public MessageModel() {
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MessageModel(String uid, String message, String time) {
        this.uid = uid;
        this.message = message;
        this.time = time;
    }
}
