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

public class CreateGroupViewInvitesActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_view_invites);

        findViewById(R.id.btnCreateGroup).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // RecyclerView to display pending invites
        mRecyclerView = (RecyclerView) findViewById(R.id.rvPendingInvites);
        // changes in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new PendingInvAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

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
        DatabaseReference groupsRef = mDatabase.child("Groups").child(groupName);
        Group newGroup = new Group(groupName);
        newGroup.UserUIds.add(mAuth.getCurrentUser().getUid());
        groupsRef.setValue(newGroup);

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
        }
    }

    public static class RecyclerViewAdapter {
    }
}
