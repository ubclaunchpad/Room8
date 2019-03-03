package com.ubclaunchpad.room8;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.model.Group;

/*
 GroupActivity is the main page when the user is set up. Members of the group
 should be able to add tasks to the group.

 Members can go to a separate activity, SendInvitesActivity, to add new members to
 the group.

 Chat functionality should also be accessible here.
*/
public class GroupActivity extends AppCompatActivity implements View.OnClickListener{

    private String mStrGroupName;
    private String mStrUID;

    public static void startGroupActivity() {
        // TODO: Refactor all locations where group activity is started.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        mStrGroupName = intent.getStringExtra("groupName");
        mStrUID = intent.getStringExtra("uid");

        TextView txtGroupName = findViewById(R.id.txtGroupName);
        txtGroupName.setText(mStrGroupName);
        txtGroupName.setOnClickListener(this);

        Button btnSendInvites = findViewById(R.id.btnSendInvites);
        btnSendInvites.setOnClickListener(this);
        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(this);
        Button btnHouseRules = findViewById(R.id.btnHouseRules);
        btnHouseRules.setOnClickListener(this);
        Button btnLeaveGroup = findViewById(R.id.btnLeaveGroup);
        btnLeaveGroup.setOnClickListener(this);
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

    // Asks the user to confirm leaving the group
    private void confirmLeaveGroup() {
        AlertDialog.Builder confirmLeaveDialog = new AlertDialog.Builder(this);
        confirmLeaveDialog.setCancelable(true);
        confirmLeaveDialog.setTitle("Leave Group");
        confirmLeaveDialog.setMessage("Are you sure you want to leave " + mStrGroupName + "?");
        confirmLeaveDialog.setPositiveButton("Leave",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leaveGroup();
                        goToCreateGroupViewInvites();
                    }
                });

        confirmLeaveDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = confirmLeaveDialog.create();
        dialog.show();
    }

    // Removes user from the group, changing their status to "No group"
    private void leaveGroup() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        // Removes the group from the user
        UserService.removeUserGroup(dbRef, mStrUID);

        // Set user's status to no group
        UserService.updateUserStatus(dbRef, mStrUID, Room8Utility.UserStatus.NO_GROUP);

        // Remove user ID from their group
        final DatabaseReference groupRef = dbRef.child("Groups").child(mStrGroupName);
        groupRef.child("UserUIds").child(mStrUID).removeValue();

        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);
                if (group != null) {
                    if (group.UserUIds == null) {
                        groupRef.removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    private void goToHouseRules() {
        Intent houseRulesIntent = new Intent(GroupActivity.this, HouseRulesActivity.class);
        houseRulesIntent.putExtra("groupName", mStrGroupName);
        startActivity(houseRulesIntent);
    }

    private void goToCreateGroupViewInvites() {
        startActivity(new Intent(GroupActivity.this, CreateGroupViewInvitesActivity.class));
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
            case R.id.btnHouseRules:
                goToHouseRules();
                break;
            case R.id.btnLeaveGroup:
                confirmLeaveGroup();
                break;
        }
    }

}
