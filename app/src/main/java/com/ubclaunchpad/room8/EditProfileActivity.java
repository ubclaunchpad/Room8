package com.ubclaunchpad.room8;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.adapter.PendingInvAdapter;
import com.ubclaunchpad.room8.exception.EmptyValueException;
import com.ubclaunchpad.room8.exception.PasswordsNotMatch;
import com.ubclaunchpad.room8.model.User;
import com.ubclaunchpad.room8.Room8Utility.FirebaseEndpoint;

import java.util.HashMap;

/*
 EditProfileActivity is where existing users edit their profile. Pretty self explanatory,
 any change in here should be reflected in the current user's entry in Firebase "Users"
 and password changes should be reflected in Firebase Authentication.
*/
public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDbRef;
    private DatabaseReference mUserRef;
    private FirebaseUser mCurrUser;
    private String mCurrUserUID;

    private String firstName, lastName, email, group, status;
    private EditText etEmail, etFirstName, etLastName, etPassword, etReEnterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // Pass group name and status
        Intent intent = getIntent();
        group = intent.getStringExtra("groupName");
        status = intent.getStringExtra("groupStatus");

        findViewById(R.id.btnEditProfile).setOnClickListener(this);

        mDbRef = FirebaseDatabase.getInstance().getReference();
        mUserRef = mDbRef.child(FirebaseEndpoint.USERS);
        mCurrUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mCurrUser != null) {
            mCurrUserUID = mCurrUser.getUid();
        } else {
            Toast.makeText(this, "Invalid app state. Current user not logged in.", Toast.LENGTH_SHORT).show();
        }

        etEmail = findViewById(R.id.edit_et_email);
        etFirstName = findViewById(R.id.edit_et_firstname);
        etLastName = findViewById(R.id.edit_et_lastname);
        etPassword = findViewById(R.id.edit_et_password);
        etReEnterPassword = findViewById(R.id.edit_et_password2);

        displayUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditProfile:
                try {
                    editUserProfile();
                } catch (EmptyValueException | PasswordsNotMatch e) {}
                break;
            }
        }

    private void editUserProfile() throws EmptyValueException, PasswordsNotMatch {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String firstPassword = etPassword.getText().toString().trim();
        String secondPassword = etReEnterPassword.getText().toString().trim();

        User currentUser = new User(mCurrUserUID, firstName, lastName, email, group, status);

        if (firstName.isEmpty()) {
            throw new EmptyValueException("First name is required.", etFirstName);
        }

        if (lastName.isEmpty()) {
            throw new EmptyValueException("Last name is required.", etLastName);
        }

        if (firstPassword.isEmpty()) {
            throw new EmptyValueException("Input the new password.", etPassword);
        }

        if (secondPassword.isEmpty()) {
            throw new EmptyValueException("Input the same password with the above.", etReEnterPassword);
        }

        if (!firstPassword.equals(secondPassword)) {
            throw new PasswordsNotMatch("Your Passwords doesn't match!", etPassword);
        }

        UserService.writeUser(mDbRef, currentUser);
        mCurrUser.updatePassword(firstPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Successfully changed your profile.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void displayUserInfo() {
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null && user.Uid.equals(mCurrUserUID)) {
                        email = user.Email;
                        firstName = user.FirstName;
                        lastName = user.LastName;
                    }
                }
                etEmail.setText(email);
                etFirstName.setText(firstName);
                etLastName.setText(lastName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
