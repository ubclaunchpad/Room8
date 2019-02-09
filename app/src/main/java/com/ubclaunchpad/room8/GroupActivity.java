package com.ubclaunchpad.room8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener{

    //group name, tabs
    private TextView groupName;
    private ImageView makeTaskButton, makeChatButton, expandTasks, expandChat;
    private Button reviewRules, btnSendInvites;
    private String strGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        strGroupName = intent.getStringExtra("groupName");

        reviewRules = findViewById(R.id.imgReviewRules);
        makeTaskButton = findViewById(R.id.imgAddTask);
        makeChatButton = findViewById(R.id.imgAddChat);
        expandTasks = findViewById(R.id.imgExpandTask);
        expandChat = findViewById(R.id.imgExpandChat);
        groupName = findViewById(R.id.txtGroupName);
        groupName.setText(strGroupName);

        btnSendInvites = findViewById(R.id.btnSendInvites);

        reviewRules.setOnClickListener(this);
        makeTaskButton.setOnClickListener(this);
        makeChatButton.setOnClickListener(this);
        expandChat.setOnClickListener(this);
        expandTasks.setOnClickListener(this);
        groupName.setOnClickListener(this);
        btnSendInvites.setOnClickListener(this);
    }

    private void makeNewConversation() {

    }

    private void makeNewTask() {

    }

    //this can be made here
    private void changeGroupName() {

    }

    private void goToSendInvites() {
        Intent sendInvitesIntent = new Intent(GroupActivity.this, SendInvitesActivity.class);
        sendInvitesIntent.putExtra("groupName", strGroupName);
        startActivity(sendInvitesIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgReviewRules:
                // open rules page (new activity?)
            case R.id.imgAddTask:
                // add a task
                makeNewTask();
            case R.id.imgAddChat:
                // create a new conversation
                makeNewConversation();
            case R.id.imgExpandChat:
                // look at chat in more detail(?) unsure of functionality
            case R.id.imgExpandTask:
                // look at tasks in more detail(?) unsure of functionality
            case R.id.btnSendInvites:
                goToSendInvites();
            case R.id.txtGroupName:
                // change group name
                changeGroupName();

        }
    }
}
