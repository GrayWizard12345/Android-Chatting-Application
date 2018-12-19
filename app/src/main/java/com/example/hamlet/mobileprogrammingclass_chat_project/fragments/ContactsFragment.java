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
import com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Contact;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends Fragment {

    private ListView contactsListView;
    private ContactsArrayAdapter arrayAdapter;
    private List<Contact> contacts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contacts = new ArrayList<>();
        Contact contact = new Contact(null, "Muslimbek A", "+998901234567");
        contacts.add(contact);
        contactsListView = view.findViewById(R.id.contacts_list_view);
        arrayAdapter = new ContactsArrayAdapter(getContext(), contacts);
        contactsListView.setAdapter(arrayAdapter);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //TODO click listener for chat item
            }
        });
    }
}
