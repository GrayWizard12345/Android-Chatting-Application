package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

import android.graphics.Bitmap;

public class Message {

    private String text;
    private String date;
    private User sender;
    private User receiver;
    private Bitmap imageMessage;
    public Message(String text, String date, User sender, User receiver, Bitmap imageMessage) {
        this.text = text;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.imageMessage = imageMessage;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getSender() {
        return sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getReceiver() {
        return receiver;
    }

    public Bitmap getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(Bitmap imageMessage) {
        this.imageMessage = imageMessage;
    }
}
