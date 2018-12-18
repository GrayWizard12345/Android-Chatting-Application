package com.example.hamlet.mobileprogrammingclass_chat_project.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class bottom_navigation_menu_fragment extends Fragment {


    public bottom_navigation_menu_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_navigation_menu_layout, container, false);
    }

}
