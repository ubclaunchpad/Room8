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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.adapter.PendingInvAdapter;
import com.ubclaunchpad.room8.model.Group;
import com.ubclaunchpad.room8.model.User;
import com.ubclaunchpad.room8.Room8Utility.FirebaseEndpoint;
import com.ubclaunchpad.room8.Room8Utility.UserStatus;

import java.util.HashMap;


public class CreateGroupViewInvitesActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_view_invites);

        findViewById(R.id.btnCreateGroup).setOnClickListener(this);
        findViewById(R.id.btnEditProfile).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // RecyclerView to display pending invites
        setRecyclerView();
    }

    private void setRecyclerView() {
        mRecyclerView = findViewById(R.id.rvPendingInvites);
        DatabaseReference userRef = mDatabase.child(FirebaseEndpoint.USERS).child(mAuth.getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                HashMap<String, String> pendingInvites = (user.PendingInvites == null) ? new HashMap<String, String>() : user.PendingInvites;

                mAdapter = new PendingInvAdapter(pendingInvites);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // changes in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
    }

    private void triggerCreateGroupFlow() {
        createGroup();
    }

    private void createGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create a group");
        LayoutInflater inflater = this.getLayoutInflater();

        View viewInflated = inflater.inflate(R.layout.dialog_create_group, (ViewGroup) this.findViewById(R.id.dialog_create_group), false);

        final EditText editTextGroupName = viewInflated.findViewById(R.id.create_group_et_groupname);

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

    private void writeGroupToDatabase(String groupName) {
        String currUserUid = mAuth.getCurrentUser().getUid();

        // Update user status
        UserService.updateUserStatus(mDatabase, currUserUid, UserStatus.IN_GROUP);
        UserService.updateUserGroup(mDatabase, currUserUid, groupName);

        // Create the group
        DatabaseReference groupsRef = mDatabase.child(FirebaseEndpoint.GROUPS).child(groupName);
        Group newGroup = new Group(groupName);
        newGroup.UserUIds.add(currUserUid);
        groupsRef.setValue(newGroup);

        // Change the page to SendInvitesActivity
        Intent intent = new Intent(this, SendInvitesActivity.class);
        intent.putExtra("name", groupName);

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
