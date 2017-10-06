package com.cragchat.mobile.view.adapters.recycler;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.ProfileActivity;
import com.cragchat.mobile.model.Displayable;
import com.cragchat.mobile.model.Send;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.util.FormatUtil;

import java.util.List;

public class SendRecyclerAdapter extends RecyclerView.Adapter<SendRecyclerAdapter.ViewHolder> {

    private CragChatActivity activity;
    private List<Send> sends;
    private boolean forProfile;

    public SendRecyclerAdapter(Activity a, List<Send> sends, boolean forProfile) {
        activity = (CragChatActivity) a;
        this.sends = sends;
        this.forProfile = forProfile;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_list_send, parent, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        TextView text5;
        TextView text6;

        private ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.item_send_username);
            text2 = (TextView) itemView.findViewById(R.id.item_send_date);
            text3 = (TextView) itemView.findViewById(R.id.item_send_pitches);
            text4 = (TextView) itemView.findViewById(R.id.item_send_attempts);
            text5 = (TextView) itemView.findViewById(R.id.item_send_style);
            text6 = (TextView) itemView.findViewById(R.id.item_send_send_type);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        final Send send = sends.get(position);
        String title;
        if (forProfile) {
            title = LocalDatabase.getInstance(activity).findExact(send.getRouteId()).getName();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Displayable disp = LocalDatabase.getInstance(activity).findExact(send.getRouteId());
                    activity.launch(disp, 2);
                }
            });
        } else {
            title = send.getUserName();
        }
        holder.text1.setText(title);
        holder.text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.hasConnection()) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("username", send.getUserName());
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Must have internet connection to view user profiles.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.text2.setText(FormatUtil.getFormattedDate(send.getDate()));
        holder.text3.setText("Pitches: " + send.getPitches());
        holder.text4.setText("Attempts: " + send.getAttempts());
        holder.text5.setText("Style: " + send.getStyle());
        holder.text6.setText("Send Type: " + send.getSendType());

    }

    @Override
    public int getItemCount() {
        return sends.size();
    }
}
