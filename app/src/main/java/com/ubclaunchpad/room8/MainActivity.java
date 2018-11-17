package com.ubclaunchpad.room8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_btn_sign_up).setOnClickListener(this);
        findViewById(R.id.main_btn_login).setOnClickListener(this);
        findViewById(R.id.main_btn_group_page).setOnClickListener(this);
        findViewById(R.id.main_btn_creategroup).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_sign_up:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.main_btn_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.main_btn_group_page:
                Log.d("MainActivity","Creating Group activity from main activity");
                startActivity(new Intent(this, GroupPageActivity.class));
                break;
            case R.id.main_btn_creategroup:
                startActivity(new Intent(this, CreateGroup.class));
        }
    }
}
