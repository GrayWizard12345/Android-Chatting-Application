package com.example.hamlet.mobileprogrammingclass_chat_project.Database;

import android.content.Context;

import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Chat;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DatabaseController {

    private static FirebaseAuth auth;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseReference;

    private static ArrayList<Chat> chats;



    public DatabaseController() {
    }

    public static User authentication (Context context) {
        User user = null;
        String name;
        String phoneNumber;
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();



        return  user;
    }

    public static void retreiveChats()
    {
        chats = new ArrayList<>();


    }

}
