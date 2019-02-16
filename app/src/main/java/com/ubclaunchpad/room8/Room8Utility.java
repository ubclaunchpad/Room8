package com.ubclaunchpad.room8;

// Utility class to ensure that constant strings used across the application match
public class Room8Utility {

    // Private to avoid unnecessary instantiation
    private Room8Utility() {}

    public static class UserStatus {
        public static final String NO_GROUP = "No group";
        public static final String CREATING = "Creating group";
        public static final String IN_GROUP = "In group";
    }

    public static class FirebaseEndpoint {
        public static final String USERS = "Users";
        public static final String GROUPS = "Groups";
        public static final String CHATROOMS = "Chatrooms";

        public static final String STATUS = "Status";
        public static final String GROUP = "Group";
    }
}
