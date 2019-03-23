package com.ubclaunchpad.room8;

import com.google.firebase.database.DatabaseReference;
import com.ubclaunchpad.room8.model.Group;

/*
    Service class to write to the "Groups" entity in Firebase.
*/
public class GroupService {

    // Given a reference to the main database, add the new group as a child under Groups,
    // with the group creator's user ID and name added to it.
    public static void createNewGroup(DatabaseReference dbRef, String groupName, String userUid, String userFirstName) {
        DatabaseReference groupsRef = dbRef.child(Room8Utility.FirebaseEndpoint.GROUPS);
        groupsRef.child(groupName).child("GroupName").setValue(groupName);
        groupsRef.child(groupName).child("UserUIds").child(userUid).setValue(userFirstName);
    }
}
