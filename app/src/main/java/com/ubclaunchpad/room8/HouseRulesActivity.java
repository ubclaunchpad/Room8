package com.ubclaunchpad.room8;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.adapter.PendingInvAdapter;
import com.ubclaunchpad.room8.model.User;
import com.ubclaunchpad.room8.Room8Utility.FirebaseEndpoint;

import java.util.HashMap;

public class HouseRulesActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private DatabaseReference mDatabase;
    private String mStrGroupName;
    private String mCurrUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_rules);
        findViewById(R.id.btnAddRule).setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        validateUser();

        // Set the TextView to this Group's name
        Intent intent = getIntent();
        mStrGroupName = intent.getStringExtra("groupName");

        // RecyclerView to display house rules
        setRecyclerView();
    }

    // Ensure the current user is logged in, grab their uid
    private void validateUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Grab the current user and their uid
        FirebaseUser currUser = firebaseAuth.getCurrentUser();
        if (currUser != null) {
            mCurrUserUid = currUser.getUid();
        } else {
            Toast.makeText(this, "Invalid app state. Current user not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    // Set house rules in the RecyclerView
    private void setRecyclerView() {
        mRecyclerView = findViewById(R.id.rvPendingInvites);

    }

    @Override
    public void onClick(View v) {

    }
}
