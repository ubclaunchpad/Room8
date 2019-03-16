package com.ubclaunchpad.room8;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.model.Chat;
import com.ubclaunchpad.room8.model.Chatroom;

public class ChatService {
    public static void createNewChatroom (DatabaseReference dbRef, String groupName, String uid) {
        DatabaseReference chatRef = dbRef.child(Room8Utility.FirebaseEndpoint.CHATROOMS).child(groupName);
        Chatroom newChatroom = new Chatroom(groupName);
        chatRef.setValue(newChatroom);
        createMainChat(dbRef, groupName, uid);
    }

    public static void createMainChat(DatabaseReference dbRef, String groupName, String uid) {
        final DatabaseReference chatroomRef = dbRef.child(Room8Utility.FirebaseEndpoint.CHATROOMS).child(groupName);
        DatabaseReference chatsRef = chatroomRef.child("Chats");
        DatabaseReference newChat = chatsRef.push();
        Chat mainChat = new Chat();
        mainChat.setType(Room8Utility.ChatTypes.MAIN_CHAT);
        mainChat.addUser(uid);
        newChat.setValue(mainChat);
        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String type = datas.child("Type").getValue().toString();
                    if (type.equals(Room8Utility.ChatTypes.MAIN_CHAT)) {
                        chatroomRef.child("MainChat").setValue(datas.child("CiD").getValue().toString());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
