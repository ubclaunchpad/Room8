package com.ubclaunchpad.room8;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupPageActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference groupReference;
    private String currentGroupName;
    TextView groupNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        Log.d("GroupPage","Creating Group Activity");
        groupNameText = findViewById(R.id.textView_groupName);
        findViewById(R.id.button_rulesAndBoundaries).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getCurrentUsersGroup();
    }

    private void setGroupName() {
        Log.d("GroupPage", "Setting the group name to " + currentGroupName);
        groupNameText.setText(currentGroupName);
    }

    // Get the group that the currentUser is in
    private void getCurrentUsersGroup() {
        String testUid = "geEWrV4DmIZuwtJXmol2HBtD53U2"; // Hardcoded User
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        groupReference = mDatabase.child("Users").child(currentUser.getUid()).child("Group"); // Dynamic Version
        groupReference = mDatabase.child("Users").child(testUid); // Hardcoded version
        groupReference.addListenerForSingleValueEvent(groupListener());
    }

    // Listener that sets this GroupPage's group
    private ValueEventListener groupListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Gets the string value of the Group from the currentUser
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    currentGroupName = dataSnapshot.child("Group").getValue(String.class);
                    Log.d("Group page", "Inside the listener, the group name " + currentGroupName);
                }
                // Sets the visual TextView of the activity
                setGroupName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_sign_up:
//                startActivity(new Intent(this, SignUpActivity.class));
                Log.d("Group Page", "Clicked the Rules and Boundaries button");
                break;
        }
    }
}
