package com.ubclaunchpad.room8;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupPageActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference usersReference;
    private DatabaseReference groupMembersReference;
    private String currentGroupName;
    TextView groupNameText;
    private List<String> usersInGroup;
    private List<String> firstNamesInGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        Log.d("GroupPage", "Creating Group Activity");
        groupNameText = findViewById(R.id.textView_groupName);
        findViewById(R.id.button_rulesAndBoundaries).setOnClickListener(this);

        usersInGroup = new ArrayList<>();
        firstNamesInGroup = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        startRetrievingGroupInformation();
    }

    // Get the group that the currentUser is in
    private void startRetrievingGroupInformation() {
        String testUid = "geEWrV4DmIZuwtJXmol2HBtD53U2"; // Hardcoded User
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        usersReference = mDatabase.child("Users").child(currentUser.getUid()).child("Group"); // Dynamic Version
        usersReference = mDatabase.child("Users"); // Hardcoded version
        usersReference.child(testUid).addListenerForSingleValueEvent(userGroupListener());
    }

    private void setGroupName() {
        Log.d("GroupPage", "Setting the group name to " + currentGroupName);
        groupNameText.setText(currentGroupName);
    }

    private void retrievingUserIDs() {
        // Get list of users from Group
        groupMembersReference = mDatabase.child("Groups").child(currentGroupName).child("UserUIds");
        groupMembersReference.addListenerForSingleValueEvent(groupListener());
    }

    // Listener that sets this GroupPage's group
    private ValueEventListener userGroupListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Gets the string value of the Group from the currentUser
                currentGroupName = dataSnapshot.child("Group").getValue(String.class);
                Log.d("Group page", "Inside the listener, the group name " + currentGroupName);
                // Sets the visual TextView of the activity
                setGroupName();
                retrievingUserIDs();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Group page", "loadPost:onCancelled", databaseError.toException());
            }
        };
    }

    // Listener that gets list of members of group
    private ValueEventListener groupListener() {
        Log.d("Group page", "Got to groupListener");
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    usersInGroup.add(ds.getValue(String.class));
                    Log.d("Group page", "Inside the userID retriever, users: " + usersInGroup);
                    Toast.makeText(GroupPageActivity.this, ds.getValue(String.class), Toast.LENGTH_SHORT).show();
                }
                getListOfFirstNames();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Group page", "loadPost:onCancelled", databaseError.toException());
            }
        };
    }

    private void getListOfFirstNames() {
        for (String userID : usersInGroup) {
            usersReference.child(userID).child("FirstName").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    firstNamesInGroup.add(dataSnapshot.getValue(String.class));
                    Toast.makeText(GroupPageActivity.this, dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("Group page", "loadPost:onCancelled", databaseError.toException());
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.main_btn_sign_up:
////                startActivity(new Intent(this, SignUpActivity.class));
//                Log.d("Group Page", "Clicked the Rules and Boundaries button");
//                break;
        }
    }
}
