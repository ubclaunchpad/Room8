package com.ubclaunchpad.room8.adapter;

import android.content.Context;
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
import com.ubclaunchpad.room8.R;
import com.ubclaunchpad.room8.SendInvitesActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SentInvAdapter extends RecyclerView.Adapter<SentInvAdapter.ViewHolder>{
    private List<String> mSentInvites;
    private String mCurrGroupUid;
    private String mCurrUserEmail;
    private DatabaseReference mDbRef;
    private SendInvitesActivity sendInvitesActivity;


    // Pass in pending invites to constructor
    public SentInvAdapter(List<String> sentInvites, String userId,
                          String email, SendInvitesActivity sendInvitesActivity) {
        mSentInvites = sentInvites;
        mCurrGroupUid = userId;
        mCurrUserEmail = email;
        mDbRef = FirebaseDatabase.getInstance().getReference();
        this.sendInvitesActivity = sendInvitesActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtViewUserName;
        // We create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance
            super(itemView);
            txtViewUserName = (TextView) itemView.findViewById(R.id.from_groupname);
        }
    }
    @NonNull
    @Override
    public SentInvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View sentInviteView = inflater.inflate(R.layout.item_sent_invite, parent, false);

        // Return a new holder instance
        return new SentInvAdapter.ViewHolder(sentInviteView);
    }

    @Override
    public void onBindViewHolder(@NonNull SentInvAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        final String groupName = mSentInvites.get(position);

        // Set item views based on views and data model
        TextView textView = holder.txtViewUserName;
        textView.setText(groupName);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}