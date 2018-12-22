package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User  implements Serializable {
    private String name;
    private String phoneNumber;
    private Bitmap userIcon;
    private String password;
    private List<String> chatIds;
    private String email;

    public User(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        chatIds = new ArrayList<>();
    }



    public User() {
    }

    public User(String name) {
        this.name = name;

    }



    public User(String name, String phoneNumber, Bitmap userIcon, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userIcon = userIcon;
        this.password = password;
    }

    public User(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Bitmap getUserIcon() {
        if(this.userIcon == null)
            userIcon = BitmapFactory.decodeResource(MainActivity.mainActivityContext.getResources(),
                    R.drawable.default_profile_icon);
        return userIcon;
    }

    public void setUserIcon(Bitmap userIcon) {
        this.userIcon = userIcon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getChatIds() {
        return chatIds;
    }

    public void setChatIds(List<String> chatIds) {
        this.chatIds = chatIds;
    }
}
