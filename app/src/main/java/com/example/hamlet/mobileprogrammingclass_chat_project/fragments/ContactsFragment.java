package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hamlet.mobileprogrammingclass_chat_project.Database.DatabaseController;
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

                boolean notFound = true;
                User user = new User(contacts.get(i).getContactName(), contacts.get(i).getContactIdentifier(), contacts.get(i).getContactIdentifier());
                for (Chat chat :
                        MainActivity.chats) {
                    if (chat.getSender().getPhoneNumber().equals(user.getPhoneNumber()))
                    {
                        chatFragment = new ChatFragment();
                        chatFragment.setMessages(chat.getMessages());
                        chatFragment.setOtherEnd(chat.getSender());
                        chatFragment.setChatId(chat.getChatId());
                        MainActivity.addFragment(chatFragment, user.getName(), MainActivity.CHAT_FRAGMENT_TYPE);
                        return;
                    }
                }
                for (User exsUser :DatabaseController.existingUsers) {
                    if(exsUser.getPhoneNumber().equals(user.getPhoneNumber()))
                    {
                        user = exsUser;
                        Chat chat = new Chat(user.getEmail().replaceAll("\\.", ""), new ArrayList<Message>(), user, MainActivity.currentUser, false, 0);
                        //save chat to database
                        DatabaseController.saveChat(chat);
//                        MainActivity.chats.add(chat);
                        MainActivity.currentUser.getChatIds().add(chat.getChatId());
                        user.getChatIds().add(chat.getChatId());
                        DatabaseController.saveChatIds(MainActivity.currentUser);
                        DatabaseController.saveChatIds(user);
                        chatFragment = new ChatFragment();
                        chatFragment.setMessages(chat.getMessages());
                        chatFragment.setChatId(chat.getChatId());
                        chatFragment.setOtherEnd(chat.getSender());
                        MainActivity.addFragment(chatFragment, user.getName(), MainActivity.CHAT_FRAGMENT_TYPE);
                        notFound = false;
                        break;
                    }
                }
                if(notFound)
                    Toast.makeText(MainActivity.mainActivityContext, "This user does not have account in our System!", Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.search_menu_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
