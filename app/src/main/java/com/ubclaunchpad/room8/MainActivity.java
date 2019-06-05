package com.ubclaunchpad.room8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.SignInButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInButton googleSignInButton = findViewById(R.id.sign_in_button);
        googleSignInButton.setSize(SignInButton.SIZE_WIDE);
        googleSignInButton.setOnClickListener(this);

        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.btnLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
