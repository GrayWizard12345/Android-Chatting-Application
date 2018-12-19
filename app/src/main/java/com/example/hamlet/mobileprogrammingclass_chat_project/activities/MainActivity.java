package com.example.hamlet.mobileprogrammingclass_chat_project.activities;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.ChatsFragment;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.ContactsFragment;

import java.util.Objects;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationMenu;

    //A simple stack data structure is used to keep track of fragments
    private Stack<Fragment> fragmentStack;
    private FragmentManager fragmentManager;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews(savedInstanceState);
        fragmentStack = new Stack<>();


        fragmentManager = getSupportFragmentManager();
    }

    /*
    * Initializes all views
     */
    private void initViews(Bundle savedInstanceState){

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationMenu = findViewById(R.id.navigation_view);
        if(savedInstanceState==null){
            navigationMenu.setNavigationItemSelectedListener(this);
            navigationMenu.setCheckedItem(R.id.nav_chats);
        }

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        toggle.setDrawerSlideAnimationEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle.syncState();


        Class fragmentClass = ChatsFragment.class;
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.frame_content, fragment).commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            Fragment fragment;
            if(!fragmentStack.isEmpty())
            {
                fragment = fragmentStack.pop();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(fragment).commit();
            }
            else
            {

            }
            toggle.syncState();
            if(!fragmentStack.isEmpty())
                toolbar.setTitle(fragmentStack.peek().getTag());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_chats:
                //TODO initialize fragment class to corresponding fragment class
                fragmentClass = ChatsFragment.class;
                fragmentStack.empty();
                break;
            case R.id.nav_contacts:
                //TODO initialize fragment class to corresponding fragment class
                fragmentClass = ContactsFragment.class;
                break;
            case R.id.nav_settings:
                //TODO initialize fragment class to corresponding fragment class
                //fragmentClass = FirstFragment.class;
                break;
            default:
                fragmentClass = ContactsFragment.class;
        }
        fragmentClass = ContactsFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction().add(R.id.frame_content, fragment).addToBackStack(null).commit();
        if(!(fragment instanceof ChatsFragment))
            fragmentStack.push(fragment);
        Log.d("Fragment management", "Fragment Stack size:" + fragmentStack.size() );
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();

        return true;
    }
}


