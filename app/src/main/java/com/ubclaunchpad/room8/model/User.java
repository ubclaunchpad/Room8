package com.ubclaunchpad.room8.model;

import java.util.HashMap;

public class User {
    public String Uid;
    public String FirstName;
    public String LastName;
    public String Email;
    public String Status;
    public String Group;
    public HashMap<String, String> PendingInvites;

    public User(){}

    public User(String uid, String firstName, String lastName, String email, HashMap<String, String> pendingInvites) {
        this.Uid = uid;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.PendingInvites = pendingInvites;
    }

    // Used when the profile is edited.
    public User(String uid, String firstName, String lastName, String email, String group, String status) {
        this.Uid = uid;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.Status = status;
        this.Group = group;
    }

    // Used when an account is made.
    public User(String uid, String firstName, String lastName, String email, String status) {
        this.Uid = uid;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.Status = status;
    }
}