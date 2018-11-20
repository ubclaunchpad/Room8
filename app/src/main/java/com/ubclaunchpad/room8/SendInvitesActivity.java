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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SendInvitesActivity extends AppCompatActivity {
    Boolean flag = false;
    TextView groupName, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        email = findViewById(R.id.UsernameEmailEditText);
        groupName = findViewById(R.id.GroupNameTextView);

        Intent intent = getIntent();
        String groupNameText = intent.getStringExtra("name");
        groupName.setText(groupNameText);

        Button addMemberButton = (Button) findViewById(R.id.AddMemberButton);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                existUser();
            }
        });
    }

    private void existUser() {
        EditText mEdit = (EditText) findViewById(R.id.UsernameEmailEditText);
        final String emailInField = mEdit.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final DatabaseReference userRef = myRef.child("Users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.Email.equalsIgnoreCase(emailInField)) {
                        flag = true;
                        DatabaseReference invitesRef = userRef.child(user.Uid).child("PendingInvites");
                        if (user.PendingInvites.containsKey("empty")) {
                            HashMap<String, String> invites = new HashMap<>();
                            invitesRef.setValue(invites);
                        }
                        DatabaseReference newPendingInvRef = invitesRef.push();
                        newPendingInvRef.setValue(groupName.getText().toString());
                    }
                }

                if (!flag){
                    Toast.makeText(getApplicationContext(), "Email not associated with an user\nPlease try again", Toast.LENGTH_SHORT).show();
                }
                else if (flag){
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                flag = false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
