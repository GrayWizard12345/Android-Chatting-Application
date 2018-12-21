package com.example.hamlet.mobileprogrammingclass_chat_project.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hamlet.mobileprogrammingclass_chat_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText et_name;
    private EditText et_email;
    private EditText et_phoneNumber;
    private EditText et_password;
    private ImageButton done_btn;
    private CheckBox checkBox_password;

    FirebaseAuth auth;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
        showHidePassword();

        done_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String phone = et_phoneNumber.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(email) || TextUtils.isEmpty(name) ) {
                    Toast.makeText(RegistrationActivity.this, "Please Fill ALL fields!", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegistrationActivity.this, "Password length should be at least 6", Toast.LENGTH_SHORT).show();

                } else {
                    register(name, phone, email, password);
                }
            }
        });
    }


    public void init() {
        et_name = findViewById(R.id.editText_name);
        et_email = findViewById(R.id.editText_email);
        et_phoneNumber = findViewById(R.id.editText_phoneNumber);
        et_password = findViewById(R.id.editText_passw);
        done_btn = findViewById(R.id.imageButton_donebtn);
        checkBox_password = findViewById(R.id.checkbox_passw);

        auth = FirebaseAuth.getInstance();
    }

    private void showHidePassword() {
        checkBox_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            }
        });

    }

    private void register(final String name, final String phoneNum, final String email, String password) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(name);

                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("name", name);
                    hashmap.put("phoneNum", phoneNum);
                    hashmap.put("email", email);

                    reference.setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegistrationActivity.this, "Not possible!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
