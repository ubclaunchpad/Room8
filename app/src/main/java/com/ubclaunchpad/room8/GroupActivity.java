package com.ubclaunchpad.room8;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
 GroupActivity is the main page when the user is set up. Members of the group
 should be able to add tasks to the group.

 Members can go to a separate activity, SendInvitesActivity, to add new members to
 the group.

 Chat functionality should also be accessible here.
*/
public class GroupActivity extends AppCompatActivity implements View.OnClickListener{

    private String mStrGroupName;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        mStrGroupName = intent.getStringExtra("groupName");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView txtGroupName = findViewById(R.id.txtGroupName);
        txtGroupName.setText(mStrGroupName);
        txtGroupName.setOnClickListener(this);

        Button btnSendInvites = findViewById(R.id.btnSendInvites);
        btnSendInvites.setOnClickListener(this);
        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(this);
        Button btnHouseRules = findViewById(R.id.btnHouseRules);
        btnHouseRules.setOnClickListener(this);
        ImageButton btnAddTask = findViewById(R.id.imgBtnAddTask);
        btnAddTask.setOnClickListener(this);
    }

    private void addNewChat() {
        // TODO: Implement adding new chat(?).
    }

    private void addNewTask(final String groupTask) {
        DatabaseReference tasksRef = mDatabase.child(Room8Utility.FirebaseEndpoint.GROUPS).child(mStrGroupName).child("Tasks");
        tasksRef.child(groupTask).setValue(groupTask);
    }

    private void changeGroupName() {
        // TODO: Implement change group name.
    }

    private void goToSendInvites() {
        Intent sendInvitesIntent = new Intent(GroupActivity.this, SendInvitesActivity.class);
        sendInvitesIntent.putExtra("groupName", mStrGroupName);
        sendInvitesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(sendInvitesIntent);
    }

    private void goToEditProfile() {
        Intent editProfileIntent = new Intent(GroupActivity.this, EditProfileActivity.class);
        startActivity(editProfileIntent);
    }

    private void goToHouseRules() {
        Intent houseRulesIntent = new Intent(GroupActivity.this, HouseRulesActivity.class);
        houseRulesIntent.putExtra("groupName", mStrGroupName);
        startActivity(houseRulesIntent);
    }

    private void inputTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input the name of the task");
        LayoutInflater inflater = this.getLayoutInflater();

        View viewInflated = inflater.inflate(R.layout.dialog_create_task, (ViewGroup) this.findViewById(R.id.dialog_create_task), false);
        final EditText etTask = viewInflated.findViewById(R.id.create_group_et_groupname);

        builder.setView(viewInflated)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String groupTask = etTask.getText().toString();

                        if (groupTask.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please enter a name of the task", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            inputTask();
                        } else {
                            addNewTask(groupTask);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendInvites:
                goToSendInvites();
                break;
            case R.id.btnEditProfile:
                goToEditProfile();
                break;
            case R.id.btnHouseRules:
                goToHouseRules();
                break;
            case R.id.imgBtnAddTask:
                inputTask();
                break;
        }
    }


}
