package com.ubclaunchpad.room8.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/*
 Populates the RecyclerView. Converts the objects at certain positions of the to-be-listed
 collection into list row items that will be inserted into the RecyclerView.
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HouseRulesAdapter extends RecyclerView.Adapter {
    private List<String> mHouseRules;

    public HouseRulesAdapter(HashMap<String, String> houseRules) {
        mHouseRules = new ArrayList<String>(houseRules.values());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
