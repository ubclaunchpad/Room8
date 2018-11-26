package com.ubclaunchpad.room8;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.model.User;

public class SendInvitesActivity extends AppCompatActivity {
    Boolean flag = false;
    TextView groupName, email;
    String groupNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        email = findViewById(R.id.UsernameEmailEditText);
        groupName = findViewById(R.id.GroupNameTextView);

        // Get group name string
        Intent intent = getIntent();
        groupNameText = intent.getStringExtra("name");
        groupName.setText(groupNameText);

        Button addMemberButton = (Button) findViewById(R.id.AddMemberButton);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteExistingUser();
            }
        });
    }

    private void inviteExistingUser() {
        EditText mEdit = (EditText) findViewById(R.id.UsernameEmailEditText);
        final String invitedEmail = mEdit.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final DatabaseReference userRef = myRef.child("Users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check for existing user by email, invite if existing
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
                    finish();
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
        if (user.PendingInvites.containsValue(groupNameText)) {
            return;
        }
        // Push new invite with unique key and group name
        DatabaseReference newPendingInvRef = invitesRef.push();
        newPendingInvRef.setValue(groupNameText);
    }
}
