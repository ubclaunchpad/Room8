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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.adapter.HouseRulesAdapter;
import com.ubclaunchpad.room8.adapter.PendingInvAdapter;
import com.ubclaunchpad.room8.model.Group;
import com.ubclaunchpad.room8.model.User;
import com.ubclaunchpad.room8.Room8Utility.FirebaseEndpoint;

import java.util.ArrayList;
import java.util.List;
/*
 HouseRulesActivity can be accessed through the GroupActivity.

 Members can add rules to their group for all members to see.
*/
public class HouseRulesActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private HouseRulesAdapter mAdapter;
    private DatabaseReference mDatabase;
    private String mStrGroupName;
    private String mCurrUserUid;
    private Integer mRuleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_rules);
        findViewById(R.id.btnAddRule).setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        validateUser();

        // Set the TextView to this Group's name
        Intent intent = getIntent();
        mStrGroupName = intent.getStringExtra("groupName");
        TextView groupName = findViewById(R.id.txtGroupName);
        groupName.setText(mStrGroupName);
        // RecyclerView to display house rules
        setRecyclerView();
    }

    // Ensure the current user is logged in, grab their uid
    private void validateUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Grab the current user and their uid
        FirebaseUser currUser = firebaseAuth.getCurrentUser();
        if (currUser != null) {
            mCurrUserUid = currUser.getUid();
        } else {
            Toast.makeText(this, "Invalid app state. Current user not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    // Set house rules in the RecyclerView
    private void setRecyclerView() {
        mRecyclerView = findViewById(R.id.rvHouseRules);

        // Set the current user's group house rules in the RecyclerView
        DatabaseReference groupRef = mDatabase.child(FirebaseEndpoint.GROUPS).child(mStrGroupName);
        groupRef.addValueEventListener(new ValueEventListener() {

            // Get the current group's house rules and use it to construct an adapter for the RecyclerView
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);
                if (group != null) {
                    List<String> houseRules;
                    if (group.HouseRules == null) {
                        houseRules =  new ArrayList<>();
                        DatabaseReference groupsRef = mDatabase.child(FirebaseEndpoint.GROUPS);
                        mRuleCount = 0;
                    } else {
                         houseRules =  group.HouseRules;
                         mRuleCount = houseRules.size();
                    }

                    mAdapter = new HouseRulesAdapter(houseRules);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Use a linear layout manager to display row items vertically
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Changes in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
    }

    private void addRule() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create a rule");
        LayoutInflater inflater = this.getLayoutInflater();

        View viewInflated = inflater.inflate(R.layout.dialog_add_rule, (ViewGroup) this.findViewById(R.id.dialog_add_rule), false);
        final EditText editTextRule = viewInflated.findViewById(R.id.create_rule);

        // Set what happens for "Confirm" and "Cancel" buttons in the dialog box
        builder.setView(viewInflated)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String rule = editTextRule.getText().toString();

                        if (rule.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please enter a valid rule.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        } else {
                            writeRuleToDatabase(rule);

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

    private void writeRuleToDatabase(String rule) {
        DatabaseReference rulesRef = mDatabase.child(FirebaseEndpoint.GROUPS).child(mStrGroupName).child("HouseRules");
        rulesRef.child(String.format("%s",mRuleCount)).setValue(rule);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddRule:
                addRule();
                break;
        }
    }
}
