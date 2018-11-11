package com.ubclaunchpad.room8;

public class User {
    String FirstName;
    String LastName;
    String Email;

    public User(){}

    public User(String firstName, String lastName, String email) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
    }
}