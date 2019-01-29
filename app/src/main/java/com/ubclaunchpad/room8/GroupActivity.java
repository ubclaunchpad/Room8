package com.ubclaunchpad.room8;

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
    private Button reviewRules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        reviewRules = findViewById(R.id.imgReviewRules);
        makeTaskButton = findViewById(R.id.imgAddTask);
        makeChatButton = findViewById(R.id.imgAddChat);
        expandTasks = findViewById(R.id.imgExpandTask);
        expandChat = findViewById(R.id.imgExpandChat);
        groupName = findViewById(R.id.txtGroupName);

        reviewRules.setOnClickListener(this);
        makeTaskButton.setOnClickListener(this);
        makeChatButton.setOnClickListener(this);
        expandChat.setOnClickListener(this);
        expandTasks.setOnClickListener(this);
        groupName.setOnClickListener(this);
    }
    private void makeNewConversation() {

    }

    private void makeNewTask() {

    }

    //this can be made here
    private void changeGroupName() {

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
            case R.id.txtGroupName:
                // change group name
                changeGroupName();

        }
    }
}
