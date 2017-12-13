package com.cragchat.mobile.view.adapters.recycler;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.model.Rating;
import com.cragchat.mobile.model.realm.RealmRating;
import com.cragchat.mobile.util.FormatUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class RatingRecyclerAdapter extends RealmRecyclerViewAdapter<RealmRating, RatingRecyclerAdapter.ViewHolder> implements LifecycleObserver {

    private CragChatActivity activity;
    private Realm mRealm;

    private RatingRecyclerAdapter(@Nullable OrderedRealmCollection<RealmRating> data,
                                  boolean autoUpdate, CragChatActivity activity, Realm realm) {
        super(data, autoUpdate);
        this.activity = activity;
        this.mRealm = realm;
    }

    public static RatingRecyclerAdapter create(String entityKey, CragChatActivity activity) {
        Realm realm = Realm.getDefaultInstance();
        return new RatingRecyclerAdapter(
                realm.where(RealmRating.class).equalTo(RealmRating.FIELD_ENTITY_KEY, entityKey).findAll(),
                true,
                activity,
                realm
        );
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void disconnectListener() {
        System.out.println("Lifecycle: Actually destroyed");
        mRealm.close();
    }

    @Override
    public RatingRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RatingRecyclerAdapter.ViewHolder(
                RecyclerUtils.getItemView(parent, R.layout.item_list_rating_recent_activity));
    }

    @Override
    public void onBindViewHolder(final RatingRecyclerAdapter.ViewHolder holder, int position) {
        final RealmRating rating = getItem(position);
        holder.bind(rating, activity, false);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_rating_username)
        TextView username;
        @BindView(R.id.item_rating_date)
        TextView date;
        @Nullable
        @BindView(R.id.label)
        TextView label;
        @BindView(R.id.item_rating_yds)
        TextView yds;
        @BindView(R.id.item_rating_stars)
        RatingBar stars;
        @BindView(R.id.layout)
        RelativeLayout layout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Rating rating, final Context activity, boolean recentActivity) {
            username.setText(rating.getUsername());
            if (recentActivity) {
                String rated = "Rated " + rating.getEntityName();
                label.setText(rated);
                label.setVisibility(View.VISIBLE);
                date.setText(FormatUtil.getDateAsElapsed(rating.getDate()));
            } else {
                label.setVisibility(View.GONE);
                date.setText(FormatUtil.getFormattedDate(rating.getDate()));
            }
            yds.setText(FormatUtil.getYdsString(itemView.getContext(), rating.getYds()));
            stars.setRating((float) rating.getStars());
        }
    }
}
