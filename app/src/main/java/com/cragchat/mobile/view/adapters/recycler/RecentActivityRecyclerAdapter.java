package com.cragchat.mobile.view.adapters.recycler;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.AreaActivity;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.EditImageActivity;
import com.cragchat.mobile.activity.RouteActivity;
import com.cragchat.mobile.activity.ViewImageActivity;
import com.cragchat.mobile.model.Area;
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
    private String entityKey;

    public RecentActivityRecyclerAdapter(CragChatActivity activity, String entityKey, List<Datable> data) {
        this.activity = activity;
        this.entityKey = entityKey;
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
                    launch(entityKey, RouteActivityPagerAdapter.TAB_SENDS);

                }
            });
        } else if (holder instanceof RatingRecyclerAdapter.ViewHolder) {
            RatingRecyclerAdapter.ViewHolder vh = (RatingRecyclerAdapter.ViewHolder) holder;
            final Rating rating = (Rating) data.get(position);
            vh.bind(rating, activity, true);
            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launch(entityKey, RouteActivityPagerAdapter.TAB_RATINGS);
                }
            });
        } else if (holder instanceof ImageRecyclerViewHolder) {
            ImageRecyclerViewHolder vh = (ImageRecyclerViewHolder) holder;
            final Image image = (Image) data.get(position);
            vh.bind(image, activity);
            vh.imageCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ViewImageActivity.class);
                    if (activity instanceof RouteActivity) {
                        intent.putExtra(EditImageActivity.ENTITY_TYPE, EditImageActivity.TYPE_ROUTE);
                    } else if (activity instanceof AreaActivity) {
                        intent.putExtra(EditImageActivity.ENTITY_TYPE, EditImageActivity.TYPE_AREA);
                    }
                    intent.putExtra(NavigationUtil.IMAGE, image);
                    activity.startActivity(intent);

                }
            });
        } else if (holder instanceof RecentActivityCommentViewHolder) {
            RecentActivityCommentViewHolder vh = (RecentActivityCommentViewHolder) holder;
            final Comment comment = (Comment) data.get(position);
            vh.bind(comment);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activity instanceof RouteActivity) {
                        launchOrSwitchTab(comment.getEntityId(),
                                ((RouteActivity) activity).getTabForCommentTable(comment.getTable()));
                    } else if (activity instanceof AreaActivity) {
                        launchOrSwitchTab(comment.getEntityId(),
                                ((AreaActivity) activity).getTabForCommentTable(comment.getTable()));
                    }
                }
            });
        }

    }

    private void launchOrSwitchTab(String entityKey, int tab) {
        if (entityKey.equals(this.entityKey)) {
            if (activity instanceof RouteActivity) {
                RouteActivity routeActivity = (RouteActivity) activity;
                routeActivity.switchTab(tab);
            } else if (activity instanceof AreaActivity) {
                AreaActivity areaActivity = (AreaActivity) activity;
                areaActivity.switchTab(tab);
            }
        } else {
            launch(entityKey, tab);
        }
    }

    /*
        Launches an Area activity
     */
    private void launchArea(String entityKey, int tab) {
        Area a = Repository.getArea(entityKey,
                null);
        NavigationUtil.launch(activity, a, tab);
    }

    /*
        Attempts to launch Route activity first, if Route with entityKey does not exist then tries
        for an Area with the given entityKey.
     */
    private void launch(String entityKey, int tab) {
        Route r = Repository.getRoute(entityKey, null);
        if (r != null) {
            NavigationUtil.launch(activity, r, tab);
        } else {
            launchArea(entityKey, tab);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
