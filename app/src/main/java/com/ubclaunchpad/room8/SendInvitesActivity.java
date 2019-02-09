package com.ubclaunchpad.room8;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.model.User;
import com.ubclaunchpad.room8.Room8Utility.FirebaseEndpoint;

public class SendInvitesActivity extends AppCompatActivity implements View.OnClickListener {
    Boolean flag = false;
    TextView groupName, email;
    String groupNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        email = findViewById(R.id.UsernameEmailEditText);
        groupName = findViewById(R.id.GroupNameTextView);

        Intent intent = getIntent();
        groupNameText = intent.getStringExtra("groupName");
        groupName.setText(groupNameText);

        //findViewById(R.id.btnEditProfile).setOnClickListener(this);
        findViewById(R.id.AddMemberButton).setOnClickListener(this);
        findViewById(R.id.btnGoToGroupPage).setOnClickListener(this);
    }

    private void inviteExistingUser() {
        EditText mEdit = findViewById(R.id.UsernameEmailEditText);
        final String invitedEmail = mEdit.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (!mAuth.getCurrentUser().getEmail().equals(invitedEmail)) {
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
        } else {
            Toast.makeText(getApplicationContext(), "Can't invite yourself!", Toast.LENGTH_SHORT).show();
        }
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

    private void goToGroupActivity() {
        String groupName = groupNameText;
        Intent groupActivityIntent = new Intent(SendInvitesActivity.this, GroupActivity.class);
        groupActivityIntent.putExtra("groupName", groupName);
        startActivity(groupActivityIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddMemberButton:
                inviteExistingUser();
            case R.id.btnGoToGroupPage:
                goToGroupActivity();
        }
    }
}
