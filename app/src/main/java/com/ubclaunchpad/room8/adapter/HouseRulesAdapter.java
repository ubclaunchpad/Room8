package com.ubclaunchpad.room8.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubclaunchpad.room8.HouseRulesActivity;
import com.ubclaunchpad.room8.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 Populates the RecyclerView. Converts the objects at certain positions of the to-be-listed
 collection into list row items that will be inserted into the RecyclerView.
*/
public class HouseRulesAdapter extends RecyclerView.Adapter<HouseRulesAdapter.ViewHolder> {
    private List<String> mHouseRules;

    public HouseRulesAdapter(List<String> houseRules) {
        mHouseRules = houseRules;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtRule;
        public TextView txtRuleNum;
        // We create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance
            super(itemView);
            txtRule = (TextView) itemView.findViewById(R.id.txtRule);
            txtRuleNum = (TextView) itemView.findViewById(R.id.txtRuleNumber);
        }
    }

    // Creates a ViewHolder used to represent an element
    @NonNull
    @Override
    public HouseRulesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View houseRuleView = inflater.inflate(R.layout.item_house_rule, parent, false);

        // Return a new holder instance
        return new ViewHolder(houseRuleView);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseRulesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String groupName = mHouseRules.get(position);

        // Set item views based on views and data model
        TextView rule = viewHolder.txtRule;
        rule.setText(groupName);
        TextView ruleNum = viewHolder.txtRuleNum;
        ruleNum.setText(String.format("%s.",position+1));
    }

    // Returns the total number of items in the list
    @Override
    public int getItemCount() {
        return mHouseRules.size();
    }
}
