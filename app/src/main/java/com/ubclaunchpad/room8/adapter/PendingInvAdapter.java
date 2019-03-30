package com.ubclaunchpad.room8.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ubclaunchpad.room8.CreateGroupViewInvitesActivity;
import com.ubclaunchpad.room8.GroupActivity;
import com.ubclaunchpad.room8.LoginActivity;
import com.ubclaunchpad.room8.R;
import com.ubclaunchpad.room8.Room8Utility;
import com.ubclaunchpad.room8.UserService;

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
    private String mCurrUserUid;
    private String mCurrUserFirstName;
    private DatabaseReference mDbRef;

    // Pass in pending invites to constructor
    public PendingInvAdapter(Map<String, String> pendingInvites, String userUid,
                             String firstName) {
        mPendingInvites = new ArrayList<String>(pendingInvites.keySet());
        mCurrUserUid = userUid;
        mCurrUserFirstName = firstName;
        mDbRef = FirebaseDatabase.getInstance().getReference();
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

    // Creates a ViewHolder used to represent an element
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
        final String groupName = mPendingInvites.get(position);

        // Set item views based on views and data model
        TextView textView = viewHolder.txtViewGroupName;
        textView.setText(groupName);

        Button button = viewHolder.btnAccept;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptInvite(groupName);
                Intent groupIntent = GroupActivity.createIntent(v.getContext(), groupName, mCurrUserUid);
                v.getContext().startActivity(groupIntent);
            }
        });
    }

    private void acceptInvite(String groupName) {
        // Update group status
         UserService.updateUserStatus(mDbRef, mCurrUserUid, Room8Utility.UserStatus.IN_GROUP);

        // Update user's group
         UserService.updateUserGroup(mDbRef, mCurrUserUid, groupName);

        // Remove group from user's pending invites
        final DatabaseReference userRef = mDbRef.child(Room8Utility.FirebaseEndpoint.USERS);
        DatabaseReference invitesRef = userRef.child(mCurrUserUid).child("PendingInvites");
        invitesRef.child(groupName).removeValue();

        // Add user's ID to group
        DatabaseReference acceptedGroupRef  = mDbRef.child(Room8Utility.FirebaseEndpoint.GROUPS).child(groupName);
        acceptedGroupRef.child("UserUIds").child(mCurrUserUid).setValue(mCurrUserFirstName);

        // Remove user from SentInvitations of group
        acceptedGroupRef.child("SentInvitations").child(mCurrUserUid).removeValue();
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPendingInvites.size();
    }
}
