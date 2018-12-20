package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Chat;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Message;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.List;

public class ChatFragment extends Fragment {

    private ListView messagesListView;
    private MessagesArrayAdapter arrayAdapter;
    private List<Message> messages;
    private EditText inputText;
    private ImageButton sendButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Init messages here


        messagesListView = view.findViewById(R.id.messages_list_view);
        arrayAdapter = new MessagesArrayAdapter(getContext(), messages);
        messagesListView.setAdapter(arrayAdapter);
        inputText = view.findViewById(R.id.input_text);
        sendButton = view.findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO ACTION CLICK LISTENER FOR SEND BUTTON
                LocalDateTime time = LocalDateTime.now();
                String messageSendTime = time.toString();
                String messageText = inputText.getText() + "";


            }
        });

    }

    public void setData(List<Message> messages, Context context)
    {
        TextView messageText;
        for (Message message: messages) {
             messageText = new TextView(context);
             messageText.setText(message.getText());


        }
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
