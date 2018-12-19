package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

public class Message {

    private String text;
    private String date;
    private User sender;
    private User receiver;
    public Message(String text, String date, User sender, User receiver) {
        this.text = text;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
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
}
