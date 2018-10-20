package com.ubclaunchpad.room8;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                break;
            case R.id.forgot_password:
                break;
        }
    }
}
