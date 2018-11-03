package com.ubclaunchpad.room8;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference groupReference;
    private Group currentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getCurrentUsersGroup();
    }

    // Get the group that the currentUser is in
    private void getCurrentUsersGroup() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        groupReference = mDatabase.child("Users").child(currentUser.getUid()).child("Group");
        groupReference.addValueEventListener(groupListener());
    }

    // Listener that sets this GroupPage's group
    private ValueEventListener groupListener() {
        final GroupPageActivity self = this;
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                self.currentGroup = dataSnapshot.getValue(Group.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
    }


}
