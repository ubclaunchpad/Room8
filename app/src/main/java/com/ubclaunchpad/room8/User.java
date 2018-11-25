package com.ubclaunchpad.room8;

import java.util.HashMap;

public class User {
    String Uid;
    String FirstName;
    String LastName;
    String Email;
    HashMap<String, String> PendingInvites;
    Group Group;

    public User(){}

    public User(String uid, String firstName, String lastName, String email, HashMap<String, String> pendingInvites) {
        this.Uid = uid;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.PendingInvites = pendingInvites;
        this.Group = null;
    }

    //For editing the profile.
    public User(String uid, String firstName, String lastName, String email) {
        this.Uid = uid;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
    }
}