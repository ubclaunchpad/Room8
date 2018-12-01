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
    final int neededPassLength = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmail = findViewById(R.id.sign_up_et_email);
        mPassword = findViewById(R.id.sign_up_et_password);
        mFirstName = findViewById(R.id.sign_up_et_firstname);
        mLastName = findViewById(R.id.sign_up_et_lastname);

        mAuth = FirebaseAuth.getInstance();

        TextView txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);
        txtSignUp.setPaintFlags(txtSignUp.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        findViewById(R.id.sign_up_btn_sign_up).setOnClickListener(this);
        findViewById(R.id.txtSignIn).setOnClickListener(this);
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
        // check the password meets correct requirements
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean specialCharFlag = false;
        boolean requiredNumber = false;
        char currentchar;
        String specialCharacters = "!#$%&'()*+,-./:;<=>?@[]^_`{|}~";
        for (int i = 0; i < password.length(); i++) {
            currentchar = password.charAt(i);
            requiredNumber = i + 1 >= neededPassLength;
            if (Character.isDigit(currentchar)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(currentchar)) {
                capitalFlag = true;
            }
            else if (Character.isLowerCase(currentchar)) {
                lowerCaseFlag = true;
            }
            for (int n = 0; n < specialCharacters.length(); n++) {
                if (specialCharacters.charAt(n) == currentchar) {
                    specialCharFlag = true;
                }
            }
        }
        if (!requiredNumber) {
            mPassword.setError("Password must be at least eight characters long");
            return;
        }
        if (!numberFlag || !capitalFlag || !lowerCaseFlag || !specialCharFlag) {
            mPassword.setError("Must contain a number, capital letter, " +
                    "lowercase letter, and special character. e.g: !#$%&");
            return;
        }
//        if (!numberFlag) {
//            mPassword.setError("Password must contain a number");
//            return;
//        }
//        else if (!capitalFlag) {
//            mPassword.setError("Password must contain a capital letter");
//            return;
//        }
//        else if (!lowerCaseFlag) {
//            mPassword.setError("Password must contain a lowercase letter");
//            return;
//        }
//        else if (!specialCharFlag) {
//            mPassword.setError("Password must contain a special character e.g.!#$%&");
//            return;
//        }

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
            case R.id.sign_up_btn_sign_up:
                registerUser();
                break;
            case R.id.txtSignIn:
                Intent loginActivityIntent = new Intent(this, LoginActivity.class);
                loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginActivityIntent);
                break;
        }
    }
}
