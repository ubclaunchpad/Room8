package com.ubclaunchpad.room8;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
import com.ubclaunchpad.room8.exception.EmptyValueException;
import com.ubclaunchpad.room8.exception.PasswordsNotMatch;
import com.ubclaunchpad.room8.model.User;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference userRef;

    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private String currUserUID;
    private String firstName;
    private String lastName;
    private String email;
    private TextView edit_et_email;
    private TextView edit_et_firstname;
    private TextView edit_et_lastname;
    private TextView enter_password;
    private TextView re_enter_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        findViewById(R.id.btnEditProfile).setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        userRef = myRef.child("Users");
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        currUserUID = mAuth.getCurrentUser().getUid();

        edit_et_email = findViewById(R.id.edit_et_email);
        edit_et_firstname = findViewById(R.id.edit_et_firstname);
        edit_et_lastname = findViewById(R.id.edit_et_lastname);
        enter_password = findViewById(R.id.edit_et_password);
        re_enter_password = findViewById(R.id.edit_et_password2);

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
        String firstName = edit_et_firstname.getText().toString().trim();
        String lastName = edit_et_lastname.getText().toString().trim();
        String firstPassword = enter_password.getText().toString().trim();
        String secondPassword = re_enter_password.getText().toString().trim();

        User currentUser = new User(currUserUID, firstName, lastName, email);

        if (firstName.isEmpty()) {
            throw new EmptyValueException("First name is required.", edit_et_firstname);
        }

        if (lastName.isEmpty()) {
            throw new EmptyValueException("Last name is required.", edit_et_lastname);
        }

        if (firstPassword.isEmpty()) {
            throw new EmptyValueException("Input the new password.", enter_password);
        }

        if (secondPassword.isEmpty()) {
            throw new EmptyValueException("Input the same password with the above.", re_enter_password);
        }

        if (!firstPassword.equals(secondPassword)) {
            throw new PasswordsNotMatch("Your Passwords doesn't match!", enter_password);
        }


        userRef.child(currUserUID).setValue(currentUser);
        currUser.updatePassword(firstPassword)
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
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.Uid.equals(currUserUID)) {
                        email = user.Email;
                        firstName = user.FirstName;
                        lastName = user.LastName;
                    }
                }
                edit_et_email.setText(email);
                edit_et_firstname.setText(firstName);
                edit_et_lastname.setText(lastName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
