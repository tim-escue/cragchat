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
import com.cragchat.mobile.model.realm.RealmRoute;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

public class RouteListRecyclerAdapter extends RealmRecyclerViewAdapter<RealmRoute, RouteListRecyclerAdapter.ViewHolder> {

    private CragChatActivity activity;

    public RouteListRecyclerAdapter(@Nullable OrderedRealmCollection<RealmRoute> data, boolean autoUpdate, CragChatActivity activity) {
        super(data, autoUpdate);
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getItemView(parent));
    }

    public static View getItemView(ViewGroup root) {
        return LayoutInflater.
                from(root.getContext()).
                inflate(R.layout.displayable_recycler_row, root, false);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RealmRoute r = getItem(position);
        holder.bind(r, activity);
    }

    public void sort(String field) {
        OrderedRealmCollection<RealmRoute> data = getData();
        if (data != null) {
            updateData(data.sort(field, Sort.ASCENDING));
        }
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

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final RealmRoute r, final CragChatActivity activity) {
            text1.setText(r.getName());
            StringBuilder info = new StringBuilder();
            if (r.getRatings().equals("0")) {
                info.append("Not rated");
            } else {
                info.append(r.getYds());
                info.append(" ");
                info.append(r.getStars());
                info.append(" stars");
            }
            text2.setText(info.toString());
            icon.setImageResource(r.getType().equalsIgnoreCase("sport") ? R.drawable.bolt_img : R.drawable.nuts);
            rect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.launch(r);
                }
            });
        }
    }
}
