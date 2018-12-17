package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

import java.util.ArrayList;

public class ApplicationUser extends User {
    public ApplicationUser(int id, String name, String email, String phoneNumber) {
        super(id, name, email, phoneNumber);
    }

    public ApplicationUser(int id, String name, String email) {
        super(id, name, email);
    }

    public static ArrayList<Chat> chats;
}
