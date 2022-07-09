package com.example.techforum.Message;

public class MessageModel {
    public String uid,message;
    public long time;

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public MessageModel(String uid, String message, long time) {
        this.uid = uid;
        this.message = message;
        this.time = time;
    }
}
