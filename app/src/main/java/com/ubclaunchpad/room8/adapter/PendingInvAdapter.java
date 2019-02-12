package com.ubclaunchpad.room8.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubclaunchpad.room8.R;
import com.ubclaunchpad.room8.Room8Utility;
import com.ubclaunchpad.room8.UserService;
import com.ubclaunchpad.room8.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingInvAdapter extends
    RecyclerView.Adapter<PendingInvAdapter.ViewHolder> {
    // Store a member variable for the invites
    private List<String> mPendingInvites;
    private String mCurrUserUid;
    private DatabaseReference mDatabase;

    // Pass in the contact array into the constructor
    public PendingInvAdapter(Map<String, String> pendingInvites, String userUid) {
        mPendingInvites = new ArrayList<String>(pendingInvites.values());
        mCurrUserUid = userUid;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupNameTextView;
        Button acceptBtn;
        // We create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance
            super(itemView);

            acceptBtn = (Button) itemView.findViewById(R.id.accept_button);
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
        final String groupName = mPendingInvites.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.groupNameTextView;
        textView.setText(groupName);

        Button button = viewHolder.acceptBtn;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptInvite(groupName);
            }
        });
    }

    private void acceptInvite(String groupName) {
        // Update group status
        // UserService.updateUserStatus(mDatabase, mCurrUserUid, Room8Utility.UserStatus.IN_GROUP);

        // Update user's group
        // UserService.updateUserGroup(mDatabase, mCurrUserUid, groupName);

        // Remove group from user's pending invites
        // TODO: ==== TEST =====
//        DatabaseReference userRef = mDatabase.child(FirebaseEndpoint.USERS).child(mCurrUserUid);
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                if (user != null) {
//                    HashMap<String, String> pendingInvites = (user.PendingInvites == null) ? new HashMap<String, String>() : user.PendingInvites;
//
//                    mAdapter = new PendingInvAdapter(pendingInvites, mCurrUserUid);
//                    mRecyclerView.setAdapter(mAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
//        });

        // Add user's ID to group
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPendingInvites.size();
    }
}
