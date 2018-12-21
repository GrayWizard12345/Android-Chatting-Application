package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Message;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatFragment extends Fragment {

    private ListView messagesListView;
    private MessagesArrayAdapter arrayAdapter;
    private List<Message> messages;
    private EditText inputText;
    private ImageButton sendButton;
    private ImageButton sendImageButton;
    private User otherEnd;
    private Bitmap imageMessage;

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
        sendButton = view.findViewById(R.id.send_text_button);
        sendImageButton = view.findViewById(R.id.send_image_button);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO ACTION CLICK LISTENER FOR SEND BUTTON
                LocalDateTime time = LocalDateTime.now();
                time.format(DateTimeFormatter.BASIC_ISO_DATE);
                String messageSendTime = time.toString();
                String messageText = inputText.getText() + "";
                Message message = new Message(messageText, messageSendTime, MainActivity.currentUser, otherEnd, null);
                messages.add(message);
                messagesListView.invalidateViews();
                inputText.setText("");

            }
        });

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, MainActivity.IMG_FOR_SENDING);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!MainActivity.imageLoaded)
                        {
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }



                    }
                }).start();
            }
        });

    }

    public void setData(List<Message> messages, Context context)
    {
        TextView messageText;
        TextView messageTextTime;
        for (Message message: messages) {
             messageText = new TextView(context);
             messageTextTime = new TextView(context);
             messageText.setText(message.getText());
             messageTextTime.setText(message.getDate());

        }
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public User getOtherEnd() {
        return otherEnd;
    }

    public void setOtherEnd(User otherEnd) {
        this.otherEnd = otherEnd;
    }

    public Bitmap getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(Bitmap imageMessage) {
        this.imageMessage = imageMessage;
    }
}
