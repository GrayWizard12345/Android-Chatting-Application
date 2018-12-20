package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Chat;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Message;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.User;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    private ListView chatsListView;
    private ChatsArrayAdapter arrayAdapter;
    private List<Chat> chats;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_chats,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chats = new ArrayList<>();
        User receiver = new User(1, "Muslimbek", "muslim4ik14@gmail.com", "+998909327598");
        User sender = new User(0, "ChatterBoxSupport", "support.chatterbox@gmail.com", "+998909327598");

        ArrayList<Message> messages = new ArrayList<Message>();
        messages.add(new Message("Hello world", "12.12.12", sender, receiver));
        Chat chat = new Chat(0, messages, receiver, false, 0);
        chats.add(chat);
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
