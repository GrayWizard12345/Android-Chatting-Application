package com.example.hamlet.mobileprogrammingclass_chat_project.Database;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.ApplicationUser;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Chat;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.Message;
import com.example.hamlet.mobileprogrammingclass_chat_project.classes.User;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.ChatsFragment;
import com.example.hamlet.mobileprogrammingclass_chat_project.fragments.ContactsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity.RESULT_LOAD_IMG;
import static com.example.hamlet.mobileprogrammingclass_chat_project.activities.MainActivity.navigationMenu;

public class DatabaseController {

    private static FirebaseAuth auth;
    private static FirebaseDatabase database;
    private static DatabaseReference userRef;

    private static ArrayList<Chat> chats;

    public static ArrayList<User> existingUsers;

    public static String TAG = "Database check";

    public DatabaseController() {
    }

    public static void getUsers()
    {
        database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                existingUsers = new ArrayList<>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String name = (String) child.child("name").getValue();
                    String phoneNum = (String) child.child("phoneNum").getValue();
                    String email = (String) child.child("email").getValue();
                    String encodedImage = (String) child.child("image").getValue();
                    Bitmap bitmap = null;
                    if(encodedImage != null)
                    {
                        bitmap = MainActivity.StringToBitmap(encodedImage);
                    }
                    ArrayList<String> chatIds = new ArrayList<>();
                    long size = child.child("chatIds").getChildrenCount();
                    for (DataSnapshot c: child.child("chatIds").getChildren()) {
                        chatIds.add((String) c.child((size-1) + "").getValue());
                    }

                    User u = new User(name, phoneNum, email);
                    u.setUserIcon(bitmap);
                    u.setChatIds(chatIds);

                    existingUsers.add(u);
                    Log.d("Database event","Existing users found " + u.getName());
                    Log.d("Database event","Existing users phoneNumber " + u.getPhoneNumber());
                }
                Log.d("Database event","Existing users loaded. Size:" + existingUsers.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database fail","Failed to read value.", databaseError.toException());
            }
        });
    }




    public static void putImageToUserProfile(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        userRef = FirebaseDatabase.getInstance().getReference("Users").
                child(MainActivity.id);
        userRef.child("image").setValue(encodedImage);

    }


    public static void initCurrentUser(String id)
    {
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                String phone = (String) dataSnapshot.child("phoneNum").getValue();
                String email =(String) dataSnapshot.child("email").getValue();
                String encodedImage = (String) dataSnapshot.child("image").getValue();
                Bitmap decodedByte = null;
                if(encodedImage != null)
                {
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }

                Log.d(TAG, "onDataChange: Name:" + name + "email:" + email);
                MainActivity.currentUser = new ApplicationUser(name,email,phone);
                MainActivity.currentUser.setUserIcon(decodedByte);
                MainActivity.profileIcon = navigationMenu.getHeaderView(0).findViewById(R.id.current_profile_icon);
                MainActivity.profileIcon.setSoundEffectsEnabled(true);
                if(MainActivity.currentUser.getUserIcon() != null)
                    MainActivity.profileIcon.setImageBitmap(MainActivity.currentUser.getUserIcon());
                MainActivity.profileIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        MainActivity.mainActivityContext.startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                    }
                });

                MainActivity.userName = navigationMenu.getHeaderView(0).findViewById(R.id.account_name);
                MainActivity.userName.setText(MainActivity.currentUser.getName());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void saveChat(Chat chat)
    {

        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("Chats");
        DatabaseReference chatRef = chatsRef.child(chat.getChatId());
        chatRef.child("chatId").setValue(chat.getChatId());
        saveChatUser(chat.getSender(), chatRef.child("sender"));
        saveChatUser(chat.getReceiver(), chatRef.child("receiver"));
        chatRef.child("unread").setValue(chat.isUnread());
        chatRef.child("unreadMessagesCounter").setValue(chat.getUnreadMessagesCounter());
        chatRef.child("messages").setValue(chat.getMessages());


    }

    public static void saveChatIds(User user)
    {
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
        String id = user.getEmail().replaceAll("\\.","");
        Log.d(TAG, "saveChatIds: id:" + id);
        DatabaseReference cIds =  users.child(id).child("chatIds");

        for (int i = 0; i < user.getChatIds().size(); i++) {
//            cIds.child(i + "").push();
            cIds.child(i + "").setValue(user.getChatIds().get(i));
        }
        //cIds.setValue(user.getChatIds());
    }

    public static void saveChatUser(User user, DatabaseReference ref)
    {
        ref.child("name").setValue(user.getName());
        ref.child("phoneNum").setValue(user.getPhoneNumber());
        ref.child("email").setValue(user.getEmail());
        if(user.getUserIcon()!= null)
            ref.child("image").setValue(MainActivity.BitmapToString(user.getUserIcon()));
    }

    public static void getChatsOfCurrentUser()
    {
        database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        userRef = usersRef.child(MainActivity.id);

        DatabaseReference chatIds = userRef.child("chatIds");
        chatIds.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> chatids = new ArrayList<>();;
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    chatids.add((String) child.getValue());
                }
                MainActivity.currentUser.setChatIds(chatids);
                Log.d("Database event","Chat ids loaded. Size:" + chatids.size());
                Log.d("Database event","Chat ids loaded. Chat:" + chatids.get(0));
                Log.d("Database event","Chat ids loaded. Chat:" + chatids.get(1));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        database = FirebaseDatabase.getInstance();
                        DatabaseReference chatsRef = database.getReference("Chats");


                        for (String chatId : MainActivity.currentUser.getChatIds()) {
                            DatabaseReference chatRef = chatsRef.child(chatId);
                            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    //        private String chatId;
                                    //        private List<Message> messages;
                                    //        private User sender;
                                    //        private User receiver;
                                    //        private boolean unread;
                                    //        private int unreadMessagesCounter;
                                    String cId = (String) dataSnapshot.child("chatId").getValue();
                                    List<Message> messages = new ArrayList<>();
                                    //TODO READ MESSAGES MANUALLY
                                    User sender = null;
                                    String p = (String) dataSnapshot.child("sender").child("phoneNum").getValue();
                                    for (User u :
                                            existingUsers) {
                                        if (u.getPhoneNumber().equals(p))
                                            sender = u;
                                    }
                                    User receiver = null;
                                    p = (String) dataSnapshot.child("receiver").child("phoneNum").getValue();
                                    for (User u :
                                            existingUsers) {
                                        if (u.getPhoneNumber().equals(p))
                                            receiver = u;
                                    }
                                    boolean unread = false;
                                    int unreadMessageCounter = 0;

                                    if (sender.getPhoneNumber().equals(MainActivity.currentUser.getPhoneNumber()))
                                        sender = receiver;
                                    MainActivity.chats.add(new Chat(cId, (ArrayList<Message>) messages, sender, MainActivity.currentUser, unread, unreadMessageCounter));


                                    Log.d("Database event","Chat "+ cId + " loaded.");
                                    ChatsFragment fragment = new ChatsFragment();
                                    fragment.setChats(MainActivity.chats);
                                    MainActivity.fragmentManager.beginTransaction().
                                            replace(R.id.frame_content, fragment).addToBackStack(null).commit();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d("Database event","Chat loaded failed");
                                }
                            });
                        }
                    }
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database fail","Chat id load failed.", databaseError.toException());
            }
        });
    }

}
