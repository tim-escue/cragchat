package com.cragchat.mobile.view.adapters.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.model.Area;

import java.util.ArrayList;
import java.util.List;

public class CragsFragmentRecyclerAdapter extends RecyclerView.Adapter<CragsFragmentRecyclerAdapter.ViewHolder> {

    private List<Area> displayables;
    private CragChatActivity activity;

    public CragsFragmentRecyclerAdapter(CragChatActivity activity) {
        this.displayables = new ArrayList<>();
        this.activity = activity;
    }

    public void addItem(@NonNull Area displayable) {
        displayables.add(displayable);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return displayables.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.crag_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Area crag = displayables.get(position);
        holder.name.setText(crag.getName());
        holder.routeNumber.setText(String.valueOf(crag.getRoutes().size()));
        holder.areaNumber.setText(String.valueOf(crag.getSubAreas().size()));
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CragChatActivity)activity).launch(crag);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView routeNumber;
        TextView areaNumber;
        ImageView icon;
        RelativeLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.crag_name);
            icon = itemView.findViewById(R.id.crag_icon);
            areaNumber = itemView.findViewById(R.id.number_areas);
            routeNumber = itemView.findViewById(R.id.number_routes);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
