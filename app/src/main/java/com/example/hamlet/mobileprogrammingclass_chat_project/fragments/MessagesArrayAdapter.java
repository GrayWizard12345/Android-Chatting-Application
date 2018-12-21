package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Message;

import java.util.List;

public class MessagesArrayAdapter extends ArrayAdapter {

    private List<Message> messages;

    public MessagesArrayAdapter(@NonNull Context context, List<Message> messages) {
        super(context, R.layout.message_view_item_left);
        this.messages = messages;
    }

    @Override
    public int getCount () {
        return messages.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MessagesArrayAdapter.MessageViewHolder holder = new MessagesArrayAdapter.MessageViewHolder();
        Message message = messages.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.message_view_item_left, parent, false);
        }

        holder.messageText = convertView.findViewById(R.id.message_text);
        holder.messageDate = convertView.findViewById(R.id.message_date);
        holder.setData(message.getText(), message.getDate());

        if(message.getSender().equals(MainActivity.currentUser))
        {
            holder.messageText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.messageDate.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        }

        return convertView;
    }

    private class MessageViewHolder {

        TextView messageText;
        TextView messageDate;

        void setData(String text, String date) {

            messageDate.setText(text);
            messageDate.setText(date);
        }
    }

}
