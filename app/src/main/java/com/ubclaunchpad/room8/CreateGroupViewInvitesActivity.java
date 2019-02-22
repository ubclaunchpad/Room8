package com.ubclaunchpad.room8;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.ubclaunchpad.room8.Room8Utility.UserStatus;

import java.util.HashMap;

/*
 CreateGroupViewInvitesActivity is where NO_GROUP users go after logging in.

 In this Activity, users can either create a group they belong in, or view groups
 that have invited them.
*/
public class CreateGroupViewInvitesActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String mCurrUserUid;
    private String mCurrUserFName;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_view_invites);

        findViewById(R.id.btnCreateGroup).setOnClickListener(this);
        findViewById(R.id.btnEditProfile).setOnClickListener(this);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Grab the current user and their uid
        FirebaseUser currUser = firebaseAuth.getCurrentUser();
        if (currUser != null) {
            mCurrUserUid = currUser.getUid();
        } else {
            Toast.makeText(this, "Invalid app state. Current user not logged in.", Toast.LENGTH_SHORT).show();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // RecyclerView to display pending invites
        setRecyclerView();
    }

    // Set Group invites in the RecyclerView
    private void setRecyclerView() {
        mRecyclerView = findViewById(R.id.rvPendingInvites);

        // Set the current user's Group invites in the RecyclerView
        DatabaseReference userRef = mDatabase.child(FirebaseEndpoint.USERS).child(mCurrUserUid);
        userRef.addValueEventListener(new ValueEventListener() {
            
            // Get the current user's pending invites and use it to construct an adapter for the RecyclerView
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    HashMap<String, String> pendingInvites = (user.PendingInvites == null) ? new HashMap<String, String>() : user.PendingInvites;

                    mCurrUserFName = user.FirstName;
                    mAdapter = new PendingInvAdapter(pendingInvites, mCurrUserUid, mCurrUserFName,CreateGroupViewInvitesActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Use a linear layout manager to display row items vertically
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Changes in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
    }

    // Trampoline call to start creating a Group
    private void triggerCreateGroupFlow() {
        createGroup();
    }

    // Show a dialog box for the user to name the Group they want to create
    private void createGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create a group");
        LayoutInflater inflater = this.getLayoutInflater();

        View viewInflated = inflater.inflate(R.layout.dialog_create_group, (ViewGroup) this.findViewById(R.id.dialog_create_group), false);
        final EditText editTextGroupName = viewInflated.findViewById(R.id.create_group_et_groupname);

        // Set what happens for "Confirm" and "Cancel" buttons in the dialog box
        builder.setView(viewInflated)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String groupName = editTextGroupName.getText().toString();

                        if (groupName.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please enter a valid group name.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            createGroup();
                        } else {
                            writeGroupToDatabase(groupName);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    // Update the user status to IN_GROUP
    // Update user's group to the group they created
    // Write the new Group created to the Database
    // Now redirect to SendInvitesActivity to allow this user to invite other users
    private void writeGroupToDatabase(String groupName) {
        // Update user status
        UserService.updateUserStatus(mDatabase, mCurrUserUid, UserStatus.IN_GROUP);
        UserService.updateUserGroup(mDatabase, mCurrUserUid, groupName);

        // Create the group
        GroupService.createNewGroup(mDatabase, groupName, mCurrUserUid, mCurrUserFName);

        // Change the page to SendInvitesActivity
        Intent intent = new Intent(this, SendInvitesActivity.class);
        intent.putExtra("groupName", groupName);

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateGroup:
                triggerCreateGroupFlow();
                break;
            case R.id.btnEditProfile:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
        }
    }
}
