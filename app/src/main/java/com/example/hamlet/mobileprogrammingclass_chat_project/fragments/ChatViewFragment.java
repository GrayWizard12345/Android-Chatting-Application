package com.example.hamlet.mobileprogrammingclass_chat_project.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatViewFragment extends Fragment {


    public ChatViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_view, container, false);
    }

}
