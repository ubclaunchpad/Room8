package com.ubclaunchpad.room8;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroupViewInvitesActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_view_invites);

        findViewById(R.id.btnCreateGroup).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference groupsRef = databaseReference.child("Groups").child(groupName);
        Group newGroup = new Group(groupName);
        newGroup.UserUIds.add(mAuth.getCurrentUser().getUid());

        groupsRef.setValue(newGroup);

        startActivity(new Intent(this, SendInvitesActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateGroup:
                triggerCreateGroupFlow();
                break;
        }
    }
}
