package com.ubclaunchpad.room8;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.model.User;
import com.ubclaunchpad.room8.Room8Utility.FirebaseEndpoint;
import com.ubclaunchpad.room8.Room8Utility.UserStatus;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEmail, mPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.login_et_email);
        mPassword = findViewById(R.id.login_et_password);

        findViewById(R.id.login_btn_login).setOnClickListener(this);

        TextView txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);
    }

    private void userLogin() {
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();

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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(getApplicationContext(), "DEBUG\n"+ email + " " + password ,Toast.LENGTH_SHORT).show();

                if (task.isSuccessful()) {
                    FirebaseUser currUser = mAuth.getCurrentUser();
                    String uid = (currUser != null ? currUser.getUid() : "");

                    if (uid.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Unable to get user from Firebase.\nPlease try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(getApplicationContext(), "Success!\nYou're now logged in.", Toast.LENGTH_SHORT).show();
                    String currUserUid = mAuth.getCurrentUser().getUid();
                    startNextActivityAfterSuccessfulLogin(currUserUid);
                } else {
                    Toast.makeText(getApplicationContext(), "Oops! Log in attempt failed.\nPlease try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Handle the next Activity that opens after the user logs in. Can be one of:
    //  - CreateGroupViewInvitesActivity (if user has status of NO_GROUP)
    //  - SendInvitesActivity (if user has status of CREATING)
    //  - TODO: GroupPageActivity (if user has status of IN_GROUP)
    private void startNextActivityAfterSuccessfulLogin(final String currUserUid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(FirebaseEndpoint.USERS);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.Uid.equals(currUserUid)) {
                        if (user.Status.equals(UserStatus.NO_GROUP)) {
                            startActivity(new Intent(LoginActivity.this, CreateGroupViewInvitesActivity.class));
                        } else if (user.Status.equals(UserStatus.CREATING)) {
                            Intent intent = new Intent(LoginActivity.this, SendInvitesActivity.class);
                            intent.putExtra("name", user.Group);

                            startActivity(intent);
                        } else if (user.Status.equals(UserStatus.IN_GROUP)) {
                            // TODO: Proceed to group page
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                userLogin();
                break;
            case R.id.txtSignUp:
                Intent signUpActivityIntent = new Intent(this, SignUpActivity.class);
                signUpActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signUpActivityIntent);
                break;
        }
    }
}
