package com.ubclaunchpad.room8;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference userRef;

    private FirebaseAuth mAuth;
    private String currUserUID;
    private FirebaseUser currUser;
    private String firstName;
    private String lastName;
    private String email;
    private TextView show_email;
    private TextView edit_first_name;
    private TextView edit_last_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        userRef = myRef.child("Users");
        mAuth = FirebaseAuth.getInstance();
        currUserUID = mAuth.getCurrentUser().getUid();

        show_email = (TextView) findViewById(R.id.show_email);
        edit_first_name = (TextView) findViewById(R.id.edit_first_name);
        edit_last_name = (TextView) findViewById(R.id.edit_last_name);
        displayUserInfo();


    }

    private ValueEventListener temporaryName() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().toString().equals(mAuth.getCurrentUser().getUid())) {

                        String afirstName = ds.child("FirstName").getValue(String.class);
                        String alastName = ds.child("LastName").getValue(String.class);
                        String aemail = ds.child("Email").getValue(String.class);
                        System.out.println(afirstName);
                        System.out.println(alastName);
                        System.out.println(aemail);
                    }

                    //should store data -> settext on the layout
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
    }

    @Override
    public void onClick(View view) {

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
                show_email.setText(email);
                edit_first_name.setText(firstName);
                edit_last_name.setText(lastName);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
