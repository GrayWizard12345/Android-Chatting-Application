package com.example.hamlet.mobileprogrammingclass_chat_project.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_phoneNum;
    private EditText editText_password;
    private CheckBox checkBox_password;
    private ImageButton imageButton_done;
    private TextView textView_linkRegister;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        showHidePassword(); //to show or hide symbols in edittext when password is typing


        imageButton_done.setOnClickListener(myListener);
        textView_linkRegister.setOnClickListener(regActivity);


    }


    public void init() {
        editText_phoneNum = findViewById(R.id.editText_phoneNumber);
        editText_password = findViewById(R.id.editText_passw);
        checkBox_password = findViewById(R.id.checkbox_passw);
        imageButton_done = findViewById(R.id.imageButton_donebtn);
        textView_linkRegister = findViewById(R.id.linkToRegister);

        auth = FirebaseAuth.getInstance();
    }


    private void showHidePassword() {
        checkBox_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    editText_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else
                    editText_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            }
        });

    }


    View.OnClickListener regActivity = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phoneNum = editText_phoneNum.getText().toString();
            String password = editText_password.getText().toString();

            if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please Fill ALL fields!", Toast.LENGTH_SHORT).show();
            } else {

                auth.signInWithEmailAndPassword(phoneNum, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Auth Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        }
    };
}

