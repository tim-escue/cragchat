package com.cragchat.mobile.ui.view.adapters.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Comment;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.model.Image;
import com.cragchat.mobile.ui.model.Rating;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.model.Send;
import com.cragchat.mobile.ui.view.activity.AreaActivity;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.ui.view.activity.EditImageActivity;
import com.cragchat.mobile.ui.view.activity.RouteActivity;
import com.cragchat.mobile.ui.view.activity.ViewImageActivity;
import com.cragchat.mobile.ui.view.adapters.pager.RouteActivityPagerAdapter;
import com.cragchat.mobile.ui.view.adapters.recycler.viewholder.ImageRecyclerViewHolder;
import com.cragchat.mobile.ui.view.adapters.recycler.viewholder.RecentActivityCommentViewHolder;
import com.cragchat.mobile.util.NavigationUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tim on 7/25/17.
 */

public class RecentActivityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RATING = 2;
    private static final int TYPE_IMAGE = 3;
    private Context context;
    private List<Datable> data;
    private String entityKey;
    private Repository mRepository;

    public RecentActivityRecyclerAdapter(Context activity, String entityKey, Repository repository) {
        this.context = activity;
        this.entityKey = entityKey;
        this.data = Collections.EMPTY_LIST;
        this.mRepository = repository;
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
        Collections.sort(data,
                (Datable one, Datable two) -> two.getDate().compareTo(one.getDate()));
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
                    launch(send.getEntityKey(), RouteActivityPagerAdapter.TAB_SENDS);

                }
            });
        } else if (holder instanceof RatingRecyclerAdapter.ViewHolder) {
            RatingRecyclerAdapter.ViewHolder vh = (RatingRecyclerAdapter.ViewHolder) holder;
            final Rating rating = (Rating) data.get(position);
            vh.bind(rating, context, true);
            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launch(rating.getEntityKey(), RouteActivityPagerAdapter.TAB_RATINGS);
                }
            });
        } else if (holder instanceof ImageRecyclerViewHolder) {
            ImageRecyclerViewHolder vh = (ImageRecyclerViewHolder) holder;
            final Image image = (Image) data.get(position);
            vh.bind(image, context);
            vh.imageCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewImageActivity.class);
                    if (context instanceof RouteActivity) {
                        intent.putExtra(EditImageActivity.ENTITY_TYPE, EditImageActivity.TYPE_ROUTE);
                    } else if (context instanceof AreaActivity) {
                        intent.putExtra(EditImageActivity.ENTITY_TYPE, EditImageActivity.TYPE_AREA);
                    }
                    intent.putExtra(NavigationUtil.IMAGE, image);
                    context.startActivity(intent);

                }
            });
        } else if (holder instanceof RecentActivityCommentViewHolder) {
            RecentActivityCommentViewHolder vh = (RecentActivityCommentViewHolder) holder;
            final Comment comment = (Comment) data.get(position);
            vh.bind(comment);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof RouteActivity) {
                        launchOrSwitchTab(comment.getEntityId(),
                                ((RouteActivity) context).getTabForCommentTable(comment.getTable()));
                    } else if (context instanceof AreaActivity) {
                        launchOrSwitchTab(comment.getEntityId(),
                                ((AreaActivity) context).getTabForCommentTable(comment.getTable()));
                    }
                }
            });
        }

    }

    private void launchOrSwitchTab(String entityKey, int tab) {
        if (entityKey.equals(this.entityKey)) {
            if (context instanceof RouteActivity) {
                RouteActivity routeActivity = (RouteActivity) context;
                routeActivity.switchTab(tab);
            } else if (context instanceof AreaActivity) {
                AreaActivity areaActivity = (AreaActivity) context;
                areaActivity.switchTab(tab);
            }
        } else {
            launch(entityKey, tab);
        }
    }


    private void launchArea(String entityKey, int tab) {
        Area a = mRepository.getArea(entityKey,
                null);
        NavigationUtil.launch(context, a, tab);
    }

    private void launch(String entityKey, int tab) {
        Route r = mRepository.getRoute(entityKey, null);
        if (r != null) {
            NavigationUtil.launch(context, r, tab);
        } else {
            launchArea(entityKey, tab);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
