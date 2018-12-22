package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Chat;
import java.util.List;


public class ChatsFragment extends Fragment {

    public ListView getChatsListView() {
        return chatsListView;
    }

    public void setChatsListView(ListView chatsListView) {
        this.chatsListView = chatsListView;
    }

    private ListView chatsListView;
    private ChatsArrayAdapter arrayAdapter;
    private List<Chat> chats;

    static ChatFragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_chats,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatsListView = view.findViewById(R.id.chats_list_view);
        arrayAdapter = new ChatsArrayAdapter(getContext(), chats);
        chatsListView.setAdapter(arrayAdapter);




        chatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Chat chat = chats.get(i);
                chat.setUnread(false);
                chat.setUnreadMessagesCounter(0);
                fragment = new ChatFragment();
                fragment.setMessages(chat.getMessages());
                MainActivity.addFragment(fragment, chat.getSender().getName(), MainActivity.CHAT_FRAGMENT_TYPE);
            }
        });
    }


    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

}
