package com.ubclaunchpad.room8;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.model.ChatMessage;
import com.ubclaunchpad.room8.Room8Utility.FirebaseEndpoint;
import com.ubclaunchpad.room8.model.Chatroom;
import com.ubclaunchpad.room8.model.Group;

public class ChatroomActivity extends AppCompatActivity {
    private FirebaseListAdapter<ChatMessage> adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String group;
    private TextView groupNameText;
    private Chatroom chatroom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        Button bsend = (Button)findViewById(R.id.bsend);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        groupNameText = findViewById(R.id.group_name);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText input = (TextInputEditText) findViewById(R.id.input_text);
                ChatMessage message = new ChatMessage();
                message.setMessageText(input.getText().toString());
                message.setMessageUser(user.getEmail());
                DatabaseReference chatsRef = mDatabase.child(FirebaseEndpoint.CHATROOMS).child(group).child("Messages");
                DatabaseReference newMessage = chatsRef.push();
                newMessage.setValue(message);
                input.setText("");
            }
        });
        setGroupName();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void setGroupName() {
        String uId = user.getUid();
        DatabaseReference users = FirebaseDatabase.getInstance().getReference(FirebaseEndpoint.USERS);
        users.orderByChild("Uid").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    group = datas.child("Group").getValue().toString();
                    groupNameText.setText(group);
                    displayChatMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }

    private void displayChatMessages() {
        ListView listView = (ListView) findViewById(R.id.list_item);
        Query query = FirebaseDatabase.getInstance().getReference(FirebaseEndpoint.CHATROOMS).child(group).child("Messages");
        FirebaseListOptions<ChatMessage> messages = new FirebaseListOptions.Builder<ChatMessage>()
                .setLayout(R.layout.message)
                .setQuery(query, ChatMessage.class)
                .build();
        adapter = new FirebaseListAdapter<ChatMessage>(messages) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };
        adapter.startListening();
        listView.setAdapter(adapter);

    }
}
