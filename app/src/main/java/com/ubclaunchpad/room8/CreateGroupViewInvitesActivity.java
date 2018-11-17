package com.ubclaunchpad.room8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class CreateGroupViewInvitesActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_view_invites);
        findViewById(R.id.edit_profile).setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
        }
    }
}
