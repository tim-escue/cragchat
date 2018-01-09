package com.cragchat.mobile.ui.view.adapters.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;

import java.util.List;

public class SearchResultsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CragChatActivity activity;
    private List results;

    public SearchResultsRecyclerAdapter(CragChatActivity a, List results) {
        activity = a;
        this.results = results;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == 0
                ? new AreaListRecyclerAdapter.ViewHolder(AreaListRecyclerAdapter.getItemView(parent))
                : new RouteListRecyclerAdapter.ViewHolder(RouteListRecyclerAdapter.getItemView(parent));
    }

    public void update(List data) {
        this.results = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return results.get(position) instanceof Area ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            RouteListRecyclerAdapter.ViewHolder rHolder = (RouteListRecyclerAdapter.ViewHolder) holder;
            rHolder.bind((Route) results.get(position), activity);
        } else {
            AreaListRecyclerAdapter.ViewHolder aHolder = (AreaListRecyclerAdapter.ViewHolder) holder;
            aHolder.bind((Area) results.get(position), activity);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

}
