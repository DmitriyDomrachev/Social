package com.dmitrijdomracev.social;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText emailEditText, passwordEditText;
    Button loginBtn;
    Toolbar toolbar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailS = emailEditText.getText().toString();
                String passwordS = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(emailS) || TextUtils.isEmpty(passwordS)) {
                    Toast.makeText(getApplicationContext(), "Enter all info",
                            Toast.LENGTH_SHORT).show();
                } else if (passwordS.length() < 6){
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters",
                            Toast.LENGTH_SHORT).show();
                } else {
                    login(emailS, passwordS);
                }
            }
        });

        FirebaseApp.initializeApp(getApplicationContext());
        auth = FirebaseAuth.getInstance();



    }

    private void login(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter all info",
                    Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Authentication failed ",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}