package com.ubclaunchpad.room8.model;

import com.ubclaunchpad.room8.Room8Utility;

import java.util.ArrayList;
import java.util.List;
import com.ubclaunchpad.room8.Room8Utility.ChatTypes;

public class Chat {

    private String CiD;
    private String Type;
    private List<String> Users;
    private List<ChatMessage> Messages;

    public Chat(){}

    public Chat(String CiD) {
        this.CiD = CiD;
        this.Users = new ArrayList<>();
        this.Messages = new ArrayList<>();
    }


    public String getCiD() {
        return CiD;
    }

    public void setCiD(String ciD) {
        CiD = ciD;
    }

    public List<String> getUsers() {
        return Users;
    }

    public void setUsers(List<String> users) {
        Users = users;
    }

    public List<ChatMessage> getMessages() {
        return Messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        Messages = messages;
    }


    public void addUser(String uid) {
        Users.add(uid);
    }

    public void addMessage(ChatMessage message) {
        Messages.add(message);
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getType() {
        return this.Type;
    }

}
