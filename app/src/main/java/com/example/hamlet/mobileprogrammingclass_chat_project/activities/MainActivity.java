package com.example.hamlet.mobileprogrammingclass_chat_project.activities;


import android.content.res.Configuration;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.AboutUsFragment;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.ChatsFragment;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.ContactsFragment;


import java.util.Stack;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static DrawerLayout drawerLayout;
    public static Toolbar toolbar;
    private NavigationView navigationMenu;

    //A simple stack data structure is used to keep track of fragments
    private Stack<Fragment> fragmentStack;
    private Stack<String> fragmentTitleStack;
    private FragmentManager fragmentManager;
    private ActionBarDrawerToggle toggle;
    protected ActionBar actionBar;
    static MenuItem prevMenuItem;
    boolean mToolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentStack = new Stack<>();
        fragmentTitleStack = new Stack<>();
        initViews(savedInstanceState);


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
            navigationMenu.getMenu().getItem(0).setChecked(true);
        }

        toolbar =  findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search_menu_item);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        toggle.setDrawerSlideAnimationEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        toggle.syncState();

        findViewById(R.id.action_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarSearchOnClick(v);
            }
        });

        Class fragmentClass = ChatsFragment.class;
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).commit();
            fragmentStack.push(fragment);
            fragmentTitleStack.push(getResources().getString(R.string.app_name));
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
            Fragment prevFragment = null;
            String title = null;
            if(!fragmentStack.isEmpty())
            {
                prevFragment = fragmentStack.pop();
                title = fragmentTitleStack.pop();
                if(!fragmentStack.isEmpty())
                {
                    prevFragment = fragmentStack.peek();
                    title = fragmentTitleStack.peek();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_content, prevFragment).commit();
                }
                else
                {
                    fragmentStack.push(prevFragment);
                }

            }

            toggle.syncState();
            if(title != null)
                toolbar.setTitle(title);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (prevMenuItem == null)
            prevMenuItem = menuItem;
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_chats:
                //TODO initialize fragment class to corresponding fragment class
                fragmentClass = ChatsFragment.class;
                break;
            case R.id.nav_contacts:
                //TODO initialize fragment class to corresponding fragment class
                fragmentClass = ContactsFragment.class;
                break;
            case R.id.nav_about_us:
                //TODO initialize fragment class to corresponding fragment class
                fragmentClass = AboutUsFragment.class;
                break;
            default:
                fragmentClass = ChatsFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String title = menuItem.getTitle() + "";
        if(fragment instanceof ChatsFragment)
        {
            title = getResources().getString(R.string.app_name);
            //TODO replace hamburger button with back arrow
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        else
        {

            //TODO replace back button to hamburger button

        }
        // Insert the fragment by replacing any existing fragment
        if(!fragment.getClass().equals(fragmentStack.peek().getClass()))
        {
            fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();
            fragmentStack.push(fragment);
            fragmentTitleStack.push(title);
            Log.d("Fragment management", "Fragment Stack size:" + fragmentStack.size() );
            // Highlight the selected item has been done by NavigationView
            prevMenuItem.setChecked(false);
            menuItem.setChecked(true);
            // Set action bar title
            setTitle(title);
            // Close the navigation drawer
            drawerLayout.closeDrawers();
            prevMenuItem = menuItem;
        }


        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_button:
                toolbar.setTitle("");
                //toolbar.inflateMenu();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ActionBarDrawerToggle getToggle() {
        return toggle;
    }

    static boolean searchOpen = false;
    public void toolbarSearchOnClick(View view) {

        Log.d("Toolbar Menu Event", toolbar.getMenu().getItem(0).getTitle() + "");
        if(searchOpen)
        {
            toolbar.getMenu().getItem(0).setVisible(false);
            searchOpen = false;
        }
        else
        {
            toolbar.getMenu().getItem(0).setVisible(true);
            searchOpen = true;
        }


    }
}


