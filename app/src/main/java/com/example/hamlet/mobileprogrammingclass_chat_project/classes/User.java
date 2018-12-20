package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

import android.graphics.drawable.Icon;
import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity;

import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private Icon userIcon;
    private String password;



    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;

        this.email = email;
    }

    public User(int id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Icon getUserIcon() {
        if(this.userIcon == null)
            userIcon = Icon.createWithResource("resources", R.drawable.default_profile_icon);
        return userIcon;
    }

    public void setUserIcon(Icon userIcon) {
        this.userIcon = userIcon;
    }
}
