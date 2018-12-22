package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class ApplicationUser extends User implements Serializable {
    public ApplicationUser(String name, String email, String phoneNumber) {
        super(name, phoneNumber, email);
    }

    public static final ArrayList<Chat> chats = new ArrayList<>();
}
