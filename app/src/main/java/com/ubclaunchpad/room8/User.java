package com.ubclaunchpad.room8;

public class User {
    String Uid;
    String FirstName;
    String LastName;
    String Email;
    Group Group;

    public User(String uid, String firstName, String lastName, String email) {
        this.Uid = uid;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.Group = null;
    }

}