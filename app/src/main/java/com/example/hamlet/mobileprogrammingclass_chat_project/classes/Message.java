package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

import java.util.ArrayList;
import java.util.Date;

public class Message {

    private String text;
    private String date;

    public Message(String text, String date) {
        this.text = text;
        this.date = date;
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
}
