package com.ubclaunchpad.room8.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubclaunchpad.room8.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PendingInvAdapter extends
    RecyclerView.Adapter<PendingInvAdapter.ViewHolder> {
    // Store a member variable for the invites
    private List<String> mPendingInvites;

    // Pass in the contact array into the constructor
    public PendingInvAdapter(Map<String,String> pendingInvites) {
        mPendingInvites = new ArrayList<String>(pendingInvites.values());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupNameTextView;

        // We create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance
            super(itemView);

            groupNameTextView = (TextView) itemView.findViewById(R.id.group_name);
        }
    }

    @NonNull
    @Override
    public PendingInvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View pendingInviteView = inflater.inflate(R.layout.item_pending_invite, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(pendingInviteView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PendingInvAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String groupName = mPendingInvites.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.groupNameTextView;
        textView.setText(groupName);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPendingInvites.size();
    }
}
