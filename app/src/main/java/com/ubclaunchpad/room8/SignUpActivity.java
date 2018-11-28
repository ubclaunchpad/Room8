package com.ubclaunchpad.room8;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ubclaunchpad.room8.model.User;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEmail, mPassword, mFirstName, mLastName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_new);

        mEmail = findViewById(R.id.sign_up_et_email);
        mPassword = findViewById(R.id.sign_up_et_password);
        mFirstName = findViewById(R.id.sign_up_et_firstname);
        mLastName = findViewById(R.id.sign_up_et_lastname);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.txtLogin).setOnClickListener(this);
    }

    // Take values of sign-up fields, validate them and try to create Firebase Authentication for user
    private void registerUser() {
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String firstName = mFirstName.getText().toString().trim();
        final String lastName = mLastName.getText().toString().trim();

        // Validate sign-up fields
        if (firstName.isEmpty()) {
            mFirstName.setError("First name is required");
            mFirstName.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            mLastName.setError("Last name is required");
            mLastName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Valid email address is required");
            mEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return;
        }

        // Attempt to create Firebase Authentication for user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "DEBUG\nUID: " + mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

                    // Proceed with creating a user in the "Users" child in Firebase
                    String uid = mAuth.getCurrentUser().getUid();
                    createUser(uid, firstName, lastName, email);
                    Toast.makeText(getApplicationContext(), "You've successfully created an account!\nPlease login.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You've already registered!\nPlease login.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "DEBUG\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Something went wrong.\nPlease try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Creates a new user object in the "Users" child in Firebase
    private void createUser(String uid, String firstName, String lastName, String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        DatabaseReference userRef = myRef.child("Users").child(uid);
        userRef.setValue(new User(uid, firstName, lastName, email, null));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                registerUser();
                break;
            case R.id.txtLogin:
                Intent loginActivityIntent = new Intent(this, LoginActivity.class);
                loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginActivityIntent);
                break;
        }
    }
}
