package com.ubclaunchpad.room8;

import java.util.ArrayList;
import java.util.List;

public class Group {

    List<User> members;
    String name;

    Group(String groupName) {
        this.members = new ArrayList<>();
        this.name = groupName;
    }

    public void addUser(User newUser) {
        members.add(newUser);
        if (newUser.Group != this) {
            newUser.Group = this;
        }
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public List<User> getMembers() {
        return members;
    }
}
