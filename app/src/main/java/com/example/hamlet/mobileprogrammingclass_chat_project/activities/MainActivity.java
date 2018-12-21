package com.example.hamlet.mobileprogrammingclass_chat_project.activities;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hamlet.mobileprogrammingclass_chat_project.Database.DatabaseController;
import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.ApplicationUser;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Chat;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Contact;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.AboutUsFragment;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.ChatsFragment;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.ContactsFragment;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    public static DrawerLayout drawerLayout;
    public static Toolbar toolbar;
    public static boolean searchOpen = false;
    public static NavigationView navigationMenu;
    public static int RESULT_LOAD_IMG;
    public static int IMG_FOR_SENDING = 666;
    public static ApplicationUser currentUser;
    public static ArrayList<Contact> contacts = new ArrayList<>();
    public static ArrayList<Chat> chats = new ArrayList<>();

    //A simple stack data structure is used to keep track of fragments
    private static Stack<Fragment> fragmentStack;
    private static Stack<String> fragmentTitleStack;
    private static FragmentManager fragmentManager;
    private ActionBarDrawerToggle toggle;
    protected static ActionBar actionBar;
    static MenuItem prevMenuItem;
    public static ImageButton profileIcon;
    public static TextView userName;
    private static  Thread readContacts;
    public static MainActivity mainActivityContext;
    public static Fragment currentFragment;
    public static ArrayList<Contact> contactsSearchResult;
    public static ArrayList<Chat> chatsSearchResult;
    public static ImageButton searchButton;
    public static EditText searchEditText;
    public static TextView mTitle;
    public static boolean backProcessing = false;
    public static String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = getIntent().getStringExtra("id");
        Log.d("User event", "UserID:" + id);
        mainActivityContext = this;
        fragmentStack = new Stack<>();
        fragmentTitleStack = new Stack<>();
        fragmentManager = getSupportFragmentManager();



        readContacts = new Thread(new Runnable() {
            @Override
            public void run() {
                getContactList();
            }
        });
        readContacts.start();

        DatabaseController.initCurrentUser(id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    if(MainActivity.currentUser != null)
                    {
                        Log.d("UserNotNull", "User:" + MainActivity.currentUser);
                        DatabaseController.getChats();
                        DatabaseController.getUsers();
                        break;
                    }else {
                        try {
                            Log.d("ThreadWiat", "User:" + MainActivity.currentUser);
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        initViews(savedInstanceState);



    }

    /*
    * Initializes all views
     */
    private void initViews(Bundle savedInstanceState){

        fragmentManager = getSupportFragmentManager();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationMenu = findViewById(R.id.navigation_view);
        if(savedInstanceState==null){
            navigationMenu.setNavigationItemSelectedListener(this);
            navigationMenu.setCheckedItem(R.id.nav_chats);
        }

        toolbar =  findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search_menu_item);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        toggle.setDrawerSlideAnimationEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toggle.syncState();
        searchButton = findViewById(R.id.search_button);
        searchEditText = findViewById(R.id.search_edit_text);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Toolbar Menu Event", searchEditText.getVisibility() + "");
                if(searchOpen)
                {
                    searchEditText.setVisibility(View.INVISIBLE);
                    searchEditText.removeTextChangedListener(searchTextWatcher);
                    searchEditText.setText("");
                    searchEditText.addTextChangedListener(searchTextWatcher);
                    searchOpen = false;
                }
                else
                {
                    searchEditText.setVisibility(View.VISIBLE);
                    searchOpen = true;
                }
            }
        });

        //Search button listener
        searchEditText.addTextChangedListener(searchTextWatcher);

        Class fragmentClass = ChatsFragment.class;
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            currentFragment = fragment;
            ((ChatsFragment)fragment).setChats(chats);
            fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();
            fragmentStack.push(fragment);
            fragmentTitleStack.push(getResources().getString(R.string.app_name));
            mTitle.setText(getResources().getString(R.string.app_name));
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
        else if(!backProcessing)
        {
            backProcessing = true;
            Fragment prevFragment = null;
            String title = null;
            if(!fragmentStack.isEmpty())
            {
                prevFragment = fragmentStack.pop();
                title = fragmentTitleStack.pop();
                if(!fragmentStack.isEmpty() && !fragmentTitleStack.isEmpty())
                {
                    prevFragment = fragmentStack.peek();
                    title = fragmentTitleStack.peek();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_content, prevFragment).commit();
                }
                else
                {
                    fragmentStack.push(prevFragment);
                    fragmentTitleStack.push(title);
                }

            }

            toggle.syncState();
            if(title != null)
                mTitle.setText(title);
        }

        if(searchOpen)
        {
            searchEditText.setVisibility(View.INVISIBLE);
            searchEditText.setText("");
            searchOpen = false;
        }
        backProcessing = false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_chats:
                fragmentClass = ChatsFragment.class;
                break;
            case R.id.nav_contacts:
                if(readContacts != null) {
                    try {
                        readContacts.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                fragmentClass = ContactsFragment.class;
                break;
            case R.id.nav_about_us:
                fragmentClass = AboutUsFragment.class;
                break;
            default:
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            if(fragment instanceof ContactsFragment)
                ((ContactsFragment) fragment).setContacts(contacts);
            else if(fragment instanceof ChatsFragment)
                ((ChatsFragment)fragment).setChats(chats);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String title = menuItem.getTitle() + "";

        // Highlight the selected item has been done by NavigationView
        if(prevMenuItem != null)
            prevMenuItem.setChecked(false);
        menuItem.setChecked(true);

        // Insert the fragment by replacing any existing fragment
        if(!fragment.getClass().equals(fragmentStack.peek().getClass()))
        {
            fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();
            fragmentStack.push(fragment);
            currentFragment = fragment;
            fragmentTitleStack.push(title);
            Log.d("Fragment management", "Fragment Stack size:" + fragmentStack.size() );

            // Set action bar title
            mTitle.setText(title);
            // Close the navigation drawer
            drawerLayout.closeDrawers();
            prevMenuItem = menuItem;

            searchEditText.setVisibility(View.INVISIBLE);
            searchEditText.setText("");
            searchOpen = false;
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

    public ActionBarDrawerToggle getToggle() {
        return toggle;
    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                selectedImage = getRoundedCornerBitmap(selectedImage, 100);
                profileIcon.setImageBitmap(selectedImage);
                profileIcon.setBackground(getResources().getDrawable(R.color.transparent));
                profileIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                DatabaseController.putImageToUserProfile(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
        else if(resultCode == IMG_FOR_SENDING)
        {
            currentFragment.onActivityResult(reqCode, resultCode, data);
        }else
        {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }


    }

    public static String BitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    //Makes a bitmap image's corners rounded
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    //Get all contacts from users phone
    private void getContactList() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS
                );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Contact contact = new Contact();
                            contact.setContactName(name);
                            contact.setContactIdentifier(phoneNo);
                            contacts.add(contact);
                            Log.d("Contacts read", "Name: " + name);
                            Log.d("Contacts read", "Phone Number: " + phoneNo);
                        }
                        pCur.close();
                    }
                }
            }
            if (cur != null) {
                cur.close();
            }
        }
    }

    TextWatcher searchTextWatcher = new TextWatcher() {
        Fragment fragment = null;
        boolean once = false;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(currentFragment instanceof ContactsFragment)
            {
                contactsSearchResult = new ArrayList<>();
                for (Contact contact : contacts) {
                    if (contact.getContactName().toLowerCase().contains(s.toString().toLowerCase())
                            || contact.getContactIdentifier().toLowerCase().contains(s.toString().toLowerCase()))
                    {
                        contactsSearchResult.add(contact);
                    }
                }

                fragment = new ContactsFragment();
                ((ContactsFragment)fragment).setContacts(contactsSearchResult);
            }
            else if(currentFragment instanceof ChatsFragment)
            {
                chatsSearchResult = new ArrayList<>();
                for (Chat chat : chats) {
                    if(chat.getSender().getName().toLowerCase().contains(s.toString().toLowerCase()))
                    {
                        chatsSearchResult.add(chat);
                    }
                }
                fragment = new ChatsFragment();
                ((ChatsFragment)fragment).setChats(chatsSearchResult);
            }

            fragmentManager = getSupportFragmentManager();
            if(fragmentManager != null && fragment != null)
                fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();


        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!once)
            {
                fragmentStack.push(fragment);
                fragmentTitleStack.push(getResources().getString(R.string.search));
                mTitle.setText(getResources().getString(R.string.search));
                currentFragment = fragment;
                once = true;
            }
        }
    };
    //Change visible fragment
    public static void addFragment(Fragment fragment, String title)
    {
        fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();
        fragmentStack.push(fragment);
        fragmentTitleStack.push(title);
        currentFragment = fragment;
        mTitle.setText(title);
    }
}


