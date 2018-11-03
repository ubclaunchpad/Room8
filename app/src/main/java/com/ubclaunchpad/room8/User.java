package com.ubclaunchpad.room8;

public class User {
    String FirstName;
    String LastName;
    String Email;
    Group Group;

    public User(String firstName, String lastName, String email) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.Group = null;
    }

    public void setGroup(Group newGroup){
        this.Group = newGroup;
        if (!newGroup.getMembers().contains(this)) {
            newGroup.addUser(this);
        }
    }
}