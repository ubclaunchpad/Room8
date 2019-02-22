package com.ubclaunchpad.room8.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ubclaunchpad.room8.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 Populates the RecyclerView. Converts the objects at certain positions of the to-be-listed
 collection into list row items that will be inserted into the RecyclerView.
*/
public class PendingInvAdapter extends
    RecyclerView.Adapter<PendingInvAdapter.ViewHolder> {
    // Store a member variable for the invites
    private List<String> mPendingInvites;

    // Pass in the contact array into the constructor
    public PendingInvAdapter(Map<String,String> pendingInvites) {
        mPendingInvites = new ArrayList<String>(pendingInvites.values());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtViewGroupName;
        Button btnAccept;
        // We create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance
            super(itemView);

            btnAccept = (Button) itemView.findViewById(R.id.accept_button);
            txtViewGroupName = (TextView) itemView.findViewById(R.id.group_name);
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
        return new ViewHolder(pendingInviteView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull PendingInvAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String groupName = mPendingInvites.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.txtViewGroupName;
        textView.setText(groupName);

        Button button = viewHolder.btnAccept;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPendingInvites.size();
    }
}
