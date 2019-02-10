package com.ubclaunchpad.room8;

import com.google.firebase.database.DatabaseReference;
import com.ubclaunchpad.room8.model.Group;

/*
    Service class to write to the "Groups" entity in Firebase.
*/
public class GroupService {

    // Given a reference to the main database, create a new group given the new group's name
    // and the user that created it
    public static void createNewGroup (DatabaseReference dbRef, String groupName, String userUid) {
        DatabaseReference groupsRef = dbRef.child(Room8Utility.FirebaseEndpoint.GROUPS).child(groupName);
        Group newGroup = new Group(groupName);
        newGroup.UserUIds.add(userUid);
        groupsRef.setValue(newGroup);
    }
}
