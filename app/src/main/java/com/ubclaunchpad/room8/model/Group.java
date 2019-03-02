package com.ubclaunchpad.room8.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Group {

    public String GroupName;
    public HashMap<String,String> UserUIds;
    public List<String> HouseRules;
    public List<String> tasks;

    public Group() {
    }

    public Group(String GroupName) {
        this.GroupName = GroupName;
        this.UserUIds = new HashMap<>();
        this.tasks = new ArrayList<>();

    }
}