package com.ubclaunchpad.room8;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ubclaunchpad.room8.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/*
 SignUpActivity is where new users sign up. A successful signup creates:
    - An entry in Firebase Authentication
    - An entry in the "Users" child in Firebase Database

 The database entry is used to collect information about the user while the authentication
 entry is used to authenticate the user upon log-in.
*/
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private EditText etEmail, etPassword, etFirstName, etLastName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.sign_up_et_email);
        etPassword = findViewById(R.id.sign_up_et_password);
        etFirstName = findViewById(R.id.sign_up_et_firstname);
        etLastName = findViewById(R.id.sign_up_et_lastname);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.txtLogin).setOnClickListener(this);
    }

    // Take values of sign-up fields, validate them and try to create Firebase Authentication for user
    private void registerUser() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String firstName = etFirstName.getText().toString().trim();
        final String lastName = etLastName.getText().toString().trim();

        // Validate sign-up fields
        if (validateSignUpFields(email, password, firstName, lastName)) return;

        // Check the password meets correct requirements
        if (validatePasswordRequirements(password)) return;

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

    private boolean validateSignUpFields(final String email, String password, String firstName, String lastName) {
        if (firstName.isEmpty()) {
            etFirstName.setError("First name is required");
            etFirstName.requestFocus();
            return true;
        }

        if (lastName.isEmpty()) {
            etLastName.setError("Last name is required");
            etLastName.requestFocus();
            return true;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Incorrect email address format");
            etEmail.requestFocus();
            return true;
        }

        if (checkIfEmailExists(email)) return true;


        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return true;
        }
        return false;
    }

    private boolean checkIfEmailExists(String email) {
        EmailValidator emailValidator = new EmailValidator();
        emailValidator.setEmail(email);
        Thread EmailValidatorThread = new Thread(emailValidator);
        EmailValidatorThread.start();
        try {
            EmailValidatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!emailValidator.getValid()){
            etEmail.setError("Email address does not exist");
            etEmail.requestFocus();
            return true;
        }
        return false;
    }

    private boolean validatePasswordRequirements(String password) {
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean specialCharFlag = false;
        boolean requiredNumber = false;
        char currentchar;
        String specialCharacters = "!#$%&'()*+,-./:;<=>?@[]^_`{|}~";
        for (int i = 0; i < password.length(); i++) {
            currentchar = password.charAt(i);
            requiredNumber = i + 1 >= MIN_PASSWORD_LENGTH;
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
            etPassword.setError("Password must be at least eight characters long");
            return true;
        }
        if (!numberFlag || !capitalFlag || !lowerCaseFlag || !specialCharFlag) {
            etPassword.setError("Must contain a number, capital letter, " +
                    "lowercase letter, and special character. e.g: !#$%&");
            return true;
        }
        return false;
    }

    // Creates a new user object in the "Users" child in Firebase
    private void createUser(String uid, String firstName, String lastName, String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        User newUser = new User(uid, firstName, lastName, email, Room8Utility.UserStatus.NO_GROUP);
        UserService.writeUser(myRef, newUser);
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
