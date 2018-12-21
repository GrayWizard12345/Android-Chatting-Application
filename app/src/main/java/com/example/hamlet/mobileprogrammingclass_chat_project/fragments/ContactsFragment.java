package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Contact;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Message;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.User;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends Fragment {

    private ListView contactsListView;
    private ContactsArrayAdapter arrayAdapter;

    static ChatFragment chatFragment;

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    private List<Contact> contacts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_contacts,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactsListView = view.findViewById(R.id.contacts_list_view);
        arrayAdapter = new ContactsArrayAdapter(getContext(), contacts);
        contactsListView.setAdapter(arrayAdapter);




        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //TODO check if this contact exists in our database
                User user = new User(contacts.get(i).getContactName(), contacts.get(i).getContactIdentifier(), contacts.get(i).getContactIdentifier());
                Chat chat = new Chat(user.getPhoneNumber(), new ArrayList<Message>(), user, false, 0);

                //Todo save chat to database

                MainActivity.chats.add(chat);
                chatFragment = new ChatFragment();
                chatFragment.setMessages(chat.getMessages());
                MainActivity.addFragment(chatFragment, user.getName());
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.search_menu_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
