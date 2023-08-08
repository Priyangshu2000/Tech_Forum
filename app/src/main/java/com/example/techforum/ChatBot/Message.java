package com.example.techforum.ChatBot;

public class Message {
    Boolean sendByUser;
    String message;

    public Message(Boolean sendByUser, String message) {
        this.sendByUser = sendByUser;
        this.message = message;
    }
}
