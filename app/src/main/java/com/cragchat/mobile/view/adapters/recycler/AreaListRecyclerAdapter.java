package com.cragchat.mobile.view.adapters.recycler;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.model.realm.RealmArea;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

public class AreaListRecyclerAdapter extends RealmRecyclerViewAdapter<RealmArea, AreaListRecyclerAdapter.ViewHolder> {

    private CragChatActivity activity;

    public AreaListRecyclerAdapter(@Nullable OrderedRealmCollection<RealmArea> data, boolean autoUpdate, CragChatActivity activity) {
        super(data, autoUpdate);
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getItemView(parent));
    }

    public void sort(String field) {
        OrderedRealmCollection<RealmArea> data = getData();
        if (data != null) {
            updateData(data.sort(field, Sort.ASCENDING));
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RealmArea area = getItem(position);
        holder.bind(area, activity);
    }

    public static View getItemView(ViewGroup root) {
        return LayoutInflater.
                from(root.getContext()).
                inflate(R.layout.displayable_recycler_row, root, false);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_row_one)
        TextView text1;

        @BindView(R.id.list_row_two)
        TextView text2;

        @BindView(R.id.rectangle)
        LinearLayout rect;

        @BindView(R.id.icon)
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final RealmArea area, final CragChatActivity activity) {
            text1.setText(area.getName());
            List<String> routes = area.getRoutes();
            StringBuilder info = new StringBuilder();
            info.append(routes.size());
            info.append(" routes");
            List<String> subAreas = area.getSubAreas();
            if (subAreas.size() > 0) {
                info.append(" ");
                info.append(subAreas.size());
                info.append(" areas");
            }
            text2.setText(info.toString());
            icon.setImageResource(R.drawable.area_mountain);
            rect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.launch(area);
                }
            });
        }
    }
}
