package com.cragchat.mobile.view.adapters.recycler;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.model.Area;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class CragsFragmentRecyclerAdapter extends RealmRecyclerViewAdapter<RealmArea, CragsFragmentRecyclerAdapter.ViewHolder> {

    private CragChatActivity activity;

    public CragsFragmentRecyclerAdapter(@Nullable OrderedRealmCollection<RealmArea> data, boolean autoUpdate, CragChatActivity activity) {
        super(data, autoUpdate);
        this.activity = activity;
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
        final Area crag = getItem(position);
        holder.name.setText(crag.getName());
        holder.routeNumber.setText(String.valueOf(crag.getRoutes().size()));
        holder.areaNumber.setText(String.valueOf(crag.getSubAreas().size()));
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.launch(crag);
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
