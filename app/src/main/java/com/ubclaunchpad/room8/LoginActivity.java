package com.ubclaunchpad.room8;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEmail, mPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.login_et_email);
        mPassword = (EditText) findViewById(R.id.login_et_password);

        findViewById(R.id.login_btn_login).setOnClickListener(this);
        findViewById(R.id.login_btn_signup).setOnClickListener(this);
    }

    private void userLogin() {
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();

        if (email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Valid email address is required");
            mEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User uid: " + mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), email + " " + password ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                userLogin();
                break;
            case R.id.login_btn_signup:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.login_btn_forgot_password:
                break;
        }
    }
}
