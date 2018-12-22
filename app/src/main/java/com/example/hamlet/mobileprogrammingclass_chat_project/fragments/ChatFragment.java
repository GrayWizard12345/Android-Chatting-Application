package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hamlet.mobileprogrammingclass_chat_project.Database.DatabaseController;
import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Message;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.User;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity.BitmapToString;
import static com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity.IMG_FOR_SENDING;
import static com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity.getRoundedCornerBitmap;

public class ChatFragment extends Fragment {

    private ListView messagesListView;
    private MessagesArrayAdapter arrayAdapter;
    private ArrayList<Message> messages;
    private EditText inputText;
    private ImageButton sendButton;
    private ImageButton sendImageButton;
    private User otherEnd;
    private Bitmap imageMessage;
    private ImageView messageImage;
    private String chatId;

    private static boolean imageLoaded = false;

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
        messageImage = view.findViewById(R.id.message_image);




        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ACTION CLICK LISTENER FOR SEND BUTTON

                long time = new Date().getTime();
                String messageSendTime = (String) android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)", time);
                String messageText = inputText.getText() + "";
                final Message message = new Message(messageText, messageSendTime, MainActivity.currentUser, otherEnd, null);
                messages.add(message);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseController.saveSingleMessage(chatId, message);
                    }
                }).start();
                arrayAdapter.notifyDataSetChanged();
                messagesListView.invalidateViews();
                inputText.setText("");
                messagesListView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Select the last row so it will scroll into view...
                        messagesListView.setSelection(arrayAdapter.getCount() - 1);
                    }
                });

            }
        });

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, IMG_FOR_SENDING);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String messageSendTime = "";
                        String messageText = "";
                        final Message message = new Message(messageText, messageSendTime, MainActivity.currentUser, otherEnd, imageMessage);
                        messages.add(message);
                        messagesListView.postInvalidate();
                        while (!imageLoaded)
                        {
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        imageLoaded = false;

                        long time = new Date().getTime();
                        messageSendTime = (String) android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)", time);
                        imageMessage = Bitmap.createScaledBitmap(imageMessage, 240, 320, true);
                        message.setDate(messageSendTime);
                        message.setImageMessage(imageMessage);
                        arrayAdapter.notifyDataSetChanged();
                        messagesListView.postInvalidate();
                        messagesListView.post(new Runnable() {
                            @Override
                            public void run() {
                                // Select the last row so it will scroll into view...
                                messagesListView.setSelection(arrayAdapter.getCount() - 1);
                            }
                        });
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DatabaseController.saveSingleMessage(chatId, message);
                            }
                        }).start();


                    }
                }).start();
            }
        });

    }

    public void setData(List<Message> messages, Context context)
    {
        TextView messageText;
        TextView messageTextTime;
        ImageView messageImage;
        for (Message message: messages) {
             messageText = new TextView(context);
             messageTextTime = new TextView(context);
             messageImage = new ImageView(context);
             messageText.setText(message.getText());
             messageTextTime.setText(message.getDate());
             messageImage.setImageBitmap(message.getImageMessage());

        }
        messagesListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                messagesListView.setSelection(arrayAdapter.getCount() - 1);
            }
        });
    }

    public void setMessages(ArrayList<Message> messages) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_FOR_SENDING)
        {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = MainActivity.mainActivityContext.getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    selectedImage = getRoundedCornerBitmap(selectedImage, 100);
                    imageMessage = selectedImage;
                    //TODO Send it to the database
                    imageLoaded = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.mainActivityContext, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(MainActivity.mainActivityContext, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
