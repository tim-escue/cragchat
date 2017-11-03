package com.cragchat.mobile.view.adapters.recycler;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.ProfileActivity;
import com.cragchat.mobile.database.models.RealmRating;
import com.cragchat.mobile.util.FormatUtil;
import com.cragchat.networkapi.NetworkApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RatingRecyclerAdapter extends RealmRecyclerViewAdapter<RealmRating, RatingRecyclerAdapter.ViewHolder> {

    private CragChatActivity activity;

    public RatingRecyclerAdapter(@Nullable OrderedRealmCollection<RealmRating> data, boolean autoUpdate, CragChatActivity activity) {
        super(data, autoUpdate);
        this.activity = activity;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_rating_username) TextView text1;
        @BindView(R.id.item_rating_date) TextView text2;
        @BindView(R.id.item_rating_yds) TextView text3;
        @BindView(R.id.item_rating_stars) TextView text4;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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


        final RealmRating rating = getItem(position);
        /*String title;
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
        }*/
        holder.text1.setText(rating.getUsername());
        holder.text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkApi.isConnected(activity)) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("username", rating.getUsername());
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
