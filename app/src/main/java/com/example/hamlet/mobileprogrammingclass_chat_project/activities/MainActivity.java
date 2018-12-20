package com.example.hamlet.mobileprogrammingclass_chat_project.activities;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.DrawableRes;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Contact;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.User;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.AboutUsFragment;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.ChatsFragment;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.ContactsFragment;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    public static DrawerLayout drawerLayout;
    public static Toolbar toolbar;
    static boolean searchOpen = false;
    private NavigationView navigationMenu;
    private static int RESULT_LOAD_IMG;
    public static User currentUser = new User(1, "Muslimbek", "+998909327598");
    public static ArrayList<Contact> contacts = new ArrayList<>();

    //A simple stack data structure is used to keep track of fragments
    private Stack<Fragment> fragmentStack;
    private Stack<String> fragmentTitleStack;
    private FragmentManager fragmentManager;
    private ActionBarDrawerToggle toggle;
    protected ActionBar actionBar;
    static MenuItem prevMenuItem;
    private ImageButton profileIcon;
    private TextView userName;
    private static  Thread readContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readContacts = new Thread(new Runnable() {
            @Override
            public void run() {
                getContactList();
            }
        });
        readContacts.start();

        fragmentStack = new Stack<>();
        fragmentTitleStack = new Stack<>();
        fragmentManager = getSupportFragmentManager();

        initViews(savedInstanceState);


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
        ImageButton searchButton = findViewById(R.id.search_button);
        final EditText searchEditText = findViewById(R.id.search_edit_text);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Toolbar Menu Event", searchEditText.getVisibility() + "");
                if(searchOpen)
                {
                    searchEditText.setVisibility(View.INVISIBLE);
                    searchEditText.setText("");
                    searchOpen = false;
                }
                else
                {
                    searchEditText.setVisibility(View.VISIBLE);
                    searchOpen = true;
                }
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // TODO SEARCH HERE

                    return true;
                }
                return false;
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

        profileIcon = navigationMenu.getHeaderView(0).findViewById(R.id.current_profile_icon);
        profileIcon.setSoundEffectsEnabled(true);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        userName = navigationMenu.getHeaderView(0).findViewById(R.id.account_name);
        userName.setText(currentUser.getName());

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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
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

}


