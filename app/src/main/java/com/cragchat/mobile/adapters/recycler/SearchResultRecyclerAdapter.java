package com.cragchat.mobile.adapters.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.sql.LocalDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by tim on 7/25/17.
 */

public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<SearchResultRecyclerAdapter.DisplayableHolder>
        implements View.OnClickListener {

    private CragChatActivity activity;
    private List<Displayable> displayables;
    private RecyclerView mRecyclerView;

    public SearchResultRecyclerAdapter(CragChatActivity a, RecyclerView recyclerView) {
        activity = a;
        displayables = Collections.emptyList();
        mRecyclerView = recyclerView;
    }

    @Override
    public DisplayableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.displayable_recycler_row, parent, false);
        return new DisplayableHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DisplayableHolder holder, int position) {
        final Displayable obj = displayables.get(position);
        if (obj instanceof Route) {
            Route r = (Route) obj;
            holder.text1.setText(r.getName());
            String sters;
            String yds;
            if (r.getYds(activity) == -1) {
                yds = "Not rated";
                sters = "Not rated";
                holder.text3.setVisibility(View.GONE);
            } else {
                yds = r.getYdsString(activity, r.getYds(activity));
                sters = r.getStarsString(activity) + " stars";
                holder.text3.setVisibility(View.VISIBLE);
            }
            holder.text2.setText(yds);
            holder.text3.setText(sters);
            holder.text4.setVisibility(View.VISIBLE);
            holder.text4.setText(r.getType());
            holder.icon.setImageResource(r.getType().equalsIgnoreCase("sport") ? R.drawable.bolt_img : R.drawable.nuts);
        } else {
            Area a = (Area) obj;
            holder.text1.setText(a.getName());
            List<Displayable> within = LocalDatabase.getInstance(activity).findRoutesWithin(a);
            holder.text2.setText(within.size() + " routes");
            double average = 0;
            int size = 0;
            for (Displayable i : within) {
                Route route = (Route)i;
                if (route.getYds(activity) != -1) {
                    average += route.getStars(activity);
                    size++;
                }
            }
            String avString;
            if (size > 0) {
                average /= size;
                NumberFormat formatter = new DecimalFormat("#0.0");
                avString = formatter.format(average) + " stars";
            } else {
                avString = "No ratings";
            }
            holder.text3.setText("Route avg: " + avString);
            holder.text4.setVisibility(View.GONE);
            holder.icon.setImageResource(R.drawable.area_mountain);
        }

        holder.layout.setTag(obj);
        holder.layout.setOnClickListener(this);
    }

    public void setResults(List<Displayable> list) {
        displayables = list;
    }


    @Override
    public int getItemCount() {
        return displayables.size();
    }

    @Override
    public void onClick(View view) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        activity.launch(displayables.get(itemPosition));
    }

    public class DisplayableHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        ImageView icon;

        public DisplayableHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.rectangle);
            text1 = (TextView) itemView.findViewById(R.id.list_row_one);
            text2 = (TextView) itemView.findViewById(R.id.list_row_two);
            text3 = (TextView) itemView.findViewById(R.id.list_row_three);
            text4 = (TextView) itemView.findViewById(R.id.list_row_four);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
