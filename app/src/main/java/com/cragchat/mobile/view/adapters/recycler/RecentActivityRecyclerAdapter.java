package com.cragchat.mobile.view.adapters.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.model.Comment;
import com.cragchat.mobile.model.Datable;
import com.cragchat.mobile.model.Image;
import com.cragchat.mobile.model.Rating;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.model.Send;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.util.NavigationUtil;
import com.cragchat.mobile.view.adapters.pager.RouteActivityPagerAdapter;
import com.cragchat.mobile.view.adapters.recycler.viewholder.ImageRecyclerViewHolder;
import com.cragchat.mobile.view.adapters.recycler.viewholder.RecentActivityCommentViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 7/25/17.
 */

public class RecentActivityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RATING = 2;
    private static final int TYPE_IMAGE = 3;
    private CragChatActivity activity;
    private List<Datable> data;

    public RecentActivityRecyclerAdapter(CragChatActivity a, List<Datable> data) {
        activity = a;
        if (data != null) {
            this.data = data;
        } else {
            this.data = new ArrayList<>();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_COMMENT:
                return new RecentActivityCommentViewHolder(
                        RecentActivityCommentViewHolder.getItemView(parent, R.layout.recent_activity_comment_item));
            case TYPE_SEND:
                return new SendRecyclerAdapter.ViewHolder(SendRecyclerAdapter.getItemView(parent));
            case TYPE_RATING:
                return new RatingRecyclerAdapter.ViewHolder(
                        RecyclerUtils.getItemView(parent, R.layout.item_list_rating_recent_activity));
            case TYPE_IMAGE:
                return new ImageRecyclerViewHolder(RecyclerUtils.getItemView(parent, R.layout.recent_activity_image));
        }
        return null;
    }

    public void update(List<Datable> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = data.get(position);
        if (obj instanceof Comment) {
            return TYPE_COMMENT;
        } else if (obj instanceof Image) {
            return TYPE_IMAGE;
        } else if (obj instanceof Rating) {
            return TYPE_RATING;
        } else {
            return TYPE_SEND;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SendRecyclerAdapter.ViewHolder) {
            SendRecyclerAdapter.ViewHolder vh = (SendRecyclerAdapter.ViewHolder) holder;
            final Send send = (Send) data.get(position);
            vh.bind(send);
            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationUtil.launch(activity, Repository.getRoute(send.getEntityKey(), null),
                            RouteActivityPagerAdapter.TAB_SENDS);
                }
            });
        } else if (holder instanceof RatingRecyclerAdapter.ViewHolder) {
            RatingRecyclerAdapter.ViewHolder vh = (RatingRecyclerAdapter.ViewHolder) holder;
            final Rating rating = (Rating) data.get(position);
            vh.bind(rating, activity, true);
            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationUtil.launch(activity, Repository.getRoute(rating.getEntityKey(), null),
                            RouteActivityPagerAdapter.TAB_RATINGS);
                }
            });
        } else if (holder instanceof ImageRecyclerViewHolder) {
            ImageRecyclerViewHolder vh = (ImageRecyclerViewHolder) holder;
            final Image image = (Image) data.get(position);
            vh.bind(image, activity);
            vh.imageCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Route r = Repository.getRoute(image.getEntityKey(), null);
                    if (r != null) {
                        NavigationUtil.launch(activity, r, RouteActivityPagerAdapter.TAB_IMAGES);
                    } else {
                        NavigationUtil.launch(activity, Repository.getArea(image.getEntityKey(), null));
                    }
                }
            });
        } else if (holder instanceof RecentActivityCommentViewHolder) {
            RecentActivityCommentViewHolder vh = (RecentActivityCommentViewHolder) holder;
            final Comment comment = (Comment) data.get(position);
            vh.bind(comment);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Route r = Repository.getRoute(comment.getEntityId(), null);
                    if (r != null) {
                        NavigationUtil.launch(activity, r, RouteActivityPagerAdapter.TAB_BETA);
                    } else {
                        NavigationUtil.launch(activity, Repository.getArea(comment.getEntityId(), null));
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
