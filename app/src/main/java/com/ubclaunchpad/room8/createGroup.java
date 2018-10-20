package com.ubclaunchpad.room8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class createGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Button button = (Button) findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoommates(view);
            }
        });
    }

    public void addRoommates(View view){
        Intent intent = new Intent(this, addRoommate.class);
        startActivity(intent);
    }
}
