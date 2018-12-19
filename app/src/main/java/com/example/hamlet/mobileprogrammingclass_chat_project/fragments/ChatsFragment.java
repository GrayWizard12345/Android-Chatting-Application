package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Chat;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Contact;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    private ListView chatsListView;
    private ChatsArrayAdapter arrayAdapter;
    private List<Chat> chats;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chats = new ArrayList<>();
        chatsListView = view.findViewById(R.id.chats_list_view);
        arrayAdapter = new ChatsArrayAdapter(getContext(), chats);
        chatsListView.setAdapter(arrayAdapter);

        chatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //TODO click listener for chat item
            }
        });
    }

}
