package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Contact;

import org.w3c.dom.Text;

import java.util.List;

public class ContactsArrayAdapter extends ArrayAdapter {

    private List<Contact> contacts;

    public ContactsArrayAdapter(@NonNull Context context, List<Contact> contacts) {
        super(context, R.layout.contact_view_item);
        this.contacts = contacts;
    }

    @Override
    public int getCount () {
        return contacts.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ContactViewHolder holder = new ContactViewHolder();
        Contact contact = contacts.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.contact_view_item, parent, false);
        }

        holder.contactImageView = convertView.findViewById(R.id.profile_icon);
        holder.contactNameTextView = convertView.findViewById(R.id.contact_name);
        holder.contactIdentifierTextView = convertView.findViewById(R.id.contact_number);
        holder.setData(contact.getContactImage(), contact.getContactName(), contact.getContactIdentifier());
        return convertView;
    }

    private class ContactViewHolder {

        ImageView contactImageView;
        TextView contactNameTextView;
        TextView contactIdentifierTextView;

        void setData(String image, String name, String identifier) {

            contactNameTextView.setText(name);
            contactIdentifierTextView.setText(identifier);
            if (image != null) {
                int imageID = getContext().getResources().getIdentifier(image, "drawable", getContext().getPackageName());
                contactImageView.setImageResource(imageID);
            }
        }
    }


}
