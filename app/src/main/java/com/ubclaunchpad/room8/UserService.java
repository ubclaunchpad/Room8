package com.ubclaunchpad.room8;

import com.google.firebase.database.DatabaseReference;
import com.ubclaunchpad.room8.Room8Utility.FirebaseEndpoint;
import com.ubclaunchpad.room8.model.User;

public class UserService {

    // Given a reference to the main database, the user to be updated's uid and a status,
    // update the user's status
    public static void updateUserStatus (DatabaseReference dbRef, String uid, String status) {
        DatabaseReference statusRef = dbRef.child(FirebaseEndpoint.USERS).child(uid).child(FirebaseEndpoint.STATUS);
        statusRef.setValue(status);
    }

    // Given a reference to the main database, the user to be updated's uid and a status,
    // update the user's group
    public static void updateUserGroup (DatabaseReference dbRef, String uid, String group) {
        DatabaseReference userGroupRef = dbRef.child(FirebaseEndpoint.USERS).child(uid).child(FirebaseEndpoint.GROUP);
        userGroupRef.setValue(group);
    }

    // Given a reference to the main database, write a user to its UID
    public static void writeUser (DatabaseReference dbRef, User user) {
        DatabaseReference userRef = dbRef.child(FirebaseEndpoint.USERS);
        userRef.child(user.Uid).setValue(user);
    }
}
