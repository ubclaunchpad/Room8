package com.ubclaunchpad.room8.model;

import java.util.ArrayList;
import java.util.List;

public class Chatroom {

    public String GroupName;
    public List<ChatMessage> Messages;

    public Chatroom(String GroupName) {
        this.GroupName = GroupName;
        this.Messages = new ArrayList<>();
    }
    public Chatroom(){}

    public void addMessage(ChatMessage message) {
        Messages.add(message);
    }



}
