package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Chat;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Contact;

import java.util.List;

public class ChatsArrayAdapter extends ArrayAdapter {

    private List<Chat> chats;

    public ChatsArrayAdapter (@NonNull Context context, List<Chat> chats) {
        super(context, R.layout.chats_view_item);
        this.chats = chats;
    }

    @Override
    public int getCount () {
        return chats.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ChatsArrayAdapter.ChatViewHolder holder = new ChatsArrayAdapter.ChatViewHolder();
        Chat chat = chats.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.chats_view_item, parent, false);
        }

        holder.contactImageView = convertView.findViewById(R.id.profile_icon);
        holder.contactNameTextView = convertView.findViewById(R.id.user_name);
        holder.chatLastMessageTextView = convertView.findViewById(R.id.last_message);
        holder.messageCounterButton = convertView.findViewById(R.id.unread_message_counter);
        holder.setData(chat.getSender().getUserIcon(),
                chat.getSender().getName(), chat.getMessages().get(chat.getMessages().size()).getText(),
                chat.getUnreadMessagesCounter());
        return convertView;
    }

    private class ChatViewHolder {

        ImageView contactImageView;
        TextView contactNameTextView;
        TextView chatLastMessageTextView;
        Button messageCounterButton;

        void setData(Icon image, String name, String lastMessage, int counter) {

            contactNameTextView.setText(name);
            chatLastMessageTextView.setText(lastMessage);
            messageCounterButton.setText(String.valueOf(counter));

           /* if (image != null) {

                //contactImageView.setImageResource(image.);
            }*/
        }
    }

}
