package com.cragchat.mobile.adapters.recycler;

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
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Rating;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.util.FormatUtil;

import java.util.List;

public class RatingRecyclerAdapter extends RecyclerView.Adapter<RatingRecyclerAdapter.ViewHolder> {

    private CragChatActivity activity;
    private static LayoutInflater inflater;
    private List<Rating> ratings;
    private boolean forProfile;

    public RatingRecyclerAdapter(Activity a, List<Rating> ratings, boolean forProfile) {
        activity = (CragChatActivity) a;
        inflater = activity.getLayoutInflater();
        this.ratings = ratings;
        this.forProfile = forProfile;
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.item_rating_username);
            text2 = (TextView) itemView.findViewById(R.id.item_rating_date);
            text3 = (TextView) itemView.findViewById(R.id.item_rating_yds);
            text4 = (TextView) itemView.findViewById(R.id.item_rating_stars);
        }
    }

    @Override
    public RatingRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_list_rating, parent, false);
        return new RatingRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RatingRecyclerAdapter.ViewHolder holder, int position) {


        final Rating rating = ratings.get(position);
        String title;
        if (forProfile) {
            title = LocalDatabase.getInstance(activity).findExact(rating.getRouteId()).getName();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Displayable disp = LocalDatabase.getInstance(activity).findExact(rating.getRouteId());
                    activity.launch(disp, 2);
                }
            });
        } else {
            title = rating.getUserName();
        }
        holder.text1.setText(title);
        holder.text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.hasConnection()) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("username", rating.getUserName());
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Must have internet connection to view user profiles.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.text2.setText(FormatUtil.getFormattedDate(rating.getDate()));
        holder.text3.setText("Yds: " + activity.getResources().getStringArray(R.array.yds_options)[rating.getYds()]);
        holder.text4.setText("Stars: " + String.valueOf(rating.getStars()));

    }
}
