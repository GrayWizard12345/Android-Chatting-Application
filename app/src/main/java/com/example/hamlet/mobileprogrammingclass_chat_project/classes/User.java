package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity;

public class User {
    private int id;
    private String name;
    private String phoneNumber;
    private Bitmap userIcon;
    private String password;


    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;

    }

    public User(int id, String name, String phoneNumber, Bitmap userIcon, String password) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userIcon = userIcon;
        this.password = password;
    }

    public User(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
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
}
