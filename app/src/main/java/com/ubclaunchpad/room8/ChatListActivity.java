package com.ubclaunchpad.room8;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
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
import com.ubclaunchpad.room8.model.Chat;
import com.ubclaunchpad.room8.model.ChatMessage;
import com.ubclaunchpad.room8.model.Chatroom;

import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private FirebaseListAdapter<Chat> adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String group;
    private Chatroom chatroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        displayChat();
    }

    private void getChats() {
        String uId = user.getUid();
        DatabaseReference chats = FirebaseDatabase.getInstance().getReference(Room8Utility.FirebaseEndpoint.CHATROOMS);
        chats.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    group = datas.child("Group").getValue().toString();
                    displayChat();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }

    private void displayChat() {
        final String uid = user.getUid();
        ListView listView = (ListView) findViewById(R.id.list_chats);
        Query query = FirebaseDatabase.getInstance().getReference(Room8Utility.FirebaseEndpoint.CHATROOMS).child(group).child("Chats");
        FirebaseListOptions<Chat> chats = new FirebaseListOptions.Builder<Chat>()
                .setLayout(R.layout.chatroom)
                .setQuery(query, Chat.class)
                .build();
        adapter = new FirebaseListAdapter<Chat>(chats) {
            @Override
            protected void populateView(View v, Chat model, int position) {
                if (model.getUsers().contains(uid)) {
                    TextView chat_users = (TextView) v.findViewById(R.id.chat_users);
                    TextView last_message = (TextView) v.findViewById(R.id.last_message);
                    setUsers(chat_users, model);
                    List<ChatMessage> messages = model.getMessages();
                    last_message.setText(messages.get(messages.size() - 1).getMessageText());
                }

            }
        };
        adapter.startListening();
        listView.setAdapter(adapter);
    }

    private void setUsers(final TextView chat_users, Chat chat) {
        final List<String> users = chat.getUsers();
        DatabaseReference uRef = FirebaseDatabase.getInstance().getReference(Room8Utility.FirebaseEndpoint.USERS);
        uRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String usernames = "";
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String uid = datas.child("Uid").getValue().toString();
                    if (users.contains(uid)) {
                        usernames = usernames + datas.child("Email").getValue().toString() + ", ";
                    }
                }
                usernames = usernames.substring(0, usernames.length()-2);
                chat_users.setText(usernames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
