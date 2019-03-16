package com.ubclaunchpad.room8.model;

import java.util.ArrayList;
import java.util.List;

public class Chatroom {

    public String GroupName;
    public String MainChat;
    public List<Chat> Chats;

    public Chatroom(String GroupName) {
        this.GroupName = GroupName;
        this.Chats = new ArrayList<Chat>();
    }
    public Chatroom(){}

    public void setMainChat(String main) {
        this.MainChat = main;
    }

    public void addChat(Chat chat) {
        this.Chats.add(chat);
    }



}
