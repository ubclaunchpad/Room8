package com.ubclaunchpad.room8;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private String firstName;
    private String lastName;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = databaseReference.child("Users").child(mAuth.getCurrentUser().getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {}};
        userRef.addListenerForSingleValueEvent(eventListener);

    }

    private ValueEventListener temporaryName() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
    }

    @Override
    public void onClick(View view) {

    }
}
