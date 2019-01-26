package com.ubclaunchpad.room8;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.adapter.PendingInvAdapter;
import com.ubclaunchpad.room8.model.User;
import com.ubclaunchpad.room8.Room8Utility.FirebaseEndpoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class SendInvitesActivity extends AppCompatActivity {
    Boolean flag = false;
    TextView groupName, email;
    String groupNameText;
    String userID;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        email = findViewById(R.id.UsernameEmailEditText);
        groupName = findViewById(R.id.GroupNameTextView);

        Intent intent = getIntent();
        groupNameText = intent.getStringExtra("name");
        groupName.setText(groupNameText);
        userID = intent.getStringExtra("User ID");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button addMemberButton = (Button) findViewById(R.id.AddMemberButton);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteExistingUser();
            }
        });
        setRecyclerView();

    }
    private void setRecyclerView() {
        mRecyclerView = findViewById(R.id.sendInvitesPendingInvites);
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

    private void inviteExistingUser() {
        EditText mEdit = findViewById(R.id.UsernameEmailEditText);
        final String invitedEmail = mEdit.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final DatabaseReference userRef = myRef.child(FirebaseEndpoint.USERS);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check for existing user by email and invite
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.Email.equalsIgnoreCase(invitedEmail)) {
                        flag = true;
                        sendInvite(user, userRef);
                        break;
                    }
                }

                if (!flag) {
                    Toast.makeText(getApplicationContext(), "Email not associated with an user,\nPlease try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Success! Invitation sent.", Toast.LENGTH_SHORT).show();
                }
                flag = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendInvite(User user, DatabaseReference userRef) {
        DatabaseReference invitesRef = userRef.child(user.Uid).child("PendingInvites");
        // Check if an invite has already been sent
        if (user.PendingInvites != null && user.PendingInvites.containsValue(groupNameText)) {
            return;
        }
        DatabaseReference newPendingInvRef = invitesRef.push();
        newPendingInvRef.setValue(groupNameText);
    }
}
