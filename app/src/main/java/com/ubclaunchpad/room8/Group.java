package com.ubclaunchpad.room8;

import java.util.ArrayList;
import java.util.List;

public class Group {

    public String GroupName;
    public List<String> UserUIds;

    public Group(String GroupName) {
        this.GroupName = GroupName;
        this.UserUIds = new ArrayList<>();
    }
}