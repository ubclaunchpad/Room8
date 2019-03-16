package com.ubclaunchpad.room8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/*
 GroupActivity is the main page when the user is set up. Members of the group
 should be able to add tasks to the group.

 Members can go to a separate activity, SendInvitesActivity, to add new members to
 the group.

 Chat functionality should also be accessible here.
*/
public class GroupActivity extends AppCompatActivity implements View.OnClickListener{

    private String mStrGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        mStrGroupName = intent.getStringExtra("groupName");

        TextView txtGroupName = findViewById(R.id.txtGroupName);
        txtGroupName.setText(mStrGroupName);
        txtGroupName.setOnClickListener(this);

        Button btnSendInvites = findViewById(R.id.btnSendInvites);
        btnSendInvites.setOnClickListener(this);
        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(this);
        Button btnHouseRules = findViewById(R.id.btnHouseRules);
        btnHouseRules.setOnClickListener(this);
    }

    private void addNewChat() {
        // TODO: Implement adding new chat(?).
    }

    private void addNewTask() {
        // TODO: Implement adding tasks to group.
    }

    private void changeGroupName() {
        // TODO: Implement change group name.
    }

    private void goToSendInvites() {
        Intent sendInvitesIntent = new Intent(GroupActivity.this, SendInvitesActivity.class);
        sendInvitesIntent.putExtra("groupName", mStrGroupName);
        sendInvitesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(sendInvitesIntent);
    }

    private void goToEditProfile() {
        Intent editProfileIntent = new Intent(GroupActivity.this, EditProfileActivity.class);
        startActivity(editProfileIntent);
    }

    private void goToChat() {
        Intent chatRoomIntent = new Intent(GroupActivity.this, ChatroomActivity.class);
        startActivity(chatRoomIntent);
    }
    private void goToHouseRules() {
        Intent houseRulesIntent = new Intent(GroupActivity.this, HouseRulesActivity.class);
        houseRulesIntent.putExtra("groupName", mStrGroupName);
        startActivity(houseRulesIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendInvites:
                goToSendInvites();
                break;
            case R.id.btnEditProfile:
                goToEditProfile();
                break;
            case R.id.imgBtnAddChat:
                goToChat();
                break;
            case R.id.btnHouseRules:
                goToHouseRules();
                break;
        }
    }


}
