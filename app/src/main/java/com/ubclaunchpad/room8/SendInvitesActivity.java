package com.ubclaunchpad.room8;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.model.User;
import com.ubclaunchpad.room8.Room8Utility.FirebaseEndpoint;

/*
 SendInvitesActivity is where users invite other users to their Group. A successful invite sent:
    - Creates an entry in this group's PendingInvites
    - Creates an entry in the invited user's PendingInvites

 This activity can be reached from two places: in GroupActivity and right after creating a group in
 CreateGroupViewInvitesActivity
*/
public class SendInvitesActivity extends AppCompatActivity implements View.OnClickListener {

    private String mGroupName;
    private DatabaseReference mDbRef;
    private String mCurrentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = firebaseAuth.getCurrentUser();

        // Grab the current user and their email
        if (currUser != null) {
            mCurrentUserEmail = currUser.getUid();
        } else {
            Toast.makeText(this, "Invalid app state. Current user not logged in.", Toast.LENGTH_SHORT).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDbRef = database.getReference();

        TextView txtGroupName = findViewById(R.id.txtGroupName);

        // Set the TextView to this Group's name
        Intent intent = getIntent();
        mGroupName = intent.getStringExtra("groupName");
        txtGroupName.setText(mGroupName);

        findViewById(R.id.btnAddMember).setOnClickListener(this);
        findViewById(R.id.btnGoToGroup).setOnClickListener(this);
    }

    private void triggerInviteUserFlow() {
        inviteUser();
    }


    // Show a dialog box for the user to input the user email they want to invite
    private void inviteUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invite a user");
        LayoutInflater inflater = this.getLayoutInflater();

        View viewInflated = inflater.inflate(R.layout.dialog_invite_user, (ViewGroup) this.findViewById(R.id.dialog_invite_user), false);
        final EditText editTextUserEmail = viewInflated.findViewById(R.id.invite_user_et_email);

        // Set what happens for "Confirm" and "Cancel" buttons in the dialog box
        builder.setView(viewInflated)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String userEmail = editTextUserEmail.getText().toString();

                        if (userEmail.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            inviteUser();
                        } else if (mCurrentUserEmail.equals(userEmail)) {
                            Toast.makeText(getApplicationContext(), "Can't invite yourself.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            inviteUser();
                        }else {
                            inviteExistingUser(userEmail);
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

    // Look for the user email given by the dialog box
    private void inviteExistingUser(final String userEmail) {
        final DatabaseReference userRef = mDbRef.child(FirebaseEndpoint.USERS);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userFound = false;

                // Find existing user by email
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    // When user is found by email, send an invite
                    if (user != null && user.Email.equalsIgnoreCase(userEmail)) {
                        userFound = true;
                        sendInvite(user, userRef);
                        break;
                    }
                }

                if (!userFound) {
                    Toast.makeText(getApplicationContext(), "Email not associated with an user,\nPlease try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Success! Invitation sent.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    // Sends an invite to the specified user
    private void sendInvite(User user, DatabaseReference userRef) {
        // Check if an invite has already been sent
        if (user.PendingInvites != null && user.PendingInvites.containsValue(mGroupName)) {
            return;
        }

        // Add an invite to the user's collection of invites
        DatabaseReference invitesRef = userRef.child(user.Uid).child("PendingInvites");
        invitesRef.child(mGroupName).setValue("test");
    }

    private void goToGroupActivity() {
        Intent groupActivityIntent = new Intent(SendInvitesActivity.this, GroupActivity.class);
        groupActivityIntent.putExtra("groupName", mGroupName);
        groupActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(groupActivityIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddMember:
                triggerInviteUserFlow();
                break;
            case R.id.btnGoToGroup:
                goToGroupActivity();
                break;
        }
    }
}
