package com.cragchat.mobile.adapters;

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
import com.cragchat.mobile.sql.CheckForRouteUpdateTask;
import com.cragchat.mobile.sql.LocalDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DisplayableRecyclerAdapter extends RecyclerView.Adapter<DisplayableRecyclerAdapter.ViewHolder> {

    private List<Displayable> displayables;
    private CragChatActivity activity;
    private List<Displayable> filtered;

    public DisplayableRecyclerAdapter(CragChatActivity a, List<Displayable> routes) {
        this.displayables = routes;
        this.activity = a;
        filtered = new ArrayList<>();
    }

    public void setDisplayables(List<Displayable> list) {
        this.displayables = list;
        notifyDataSetChanged();
    }

    public void addFilter(String string) {
        for (Iterator<Displayable> routeIterator = displayables.iterator(); routeIterator.hasNext(); ) {
            Displayable r = routeIterator.next();
            if (r instanceof Route) {
                Route route = (Route) r;
                if (route.getType().equalsIgnoreCase(string)) {
                    filtered.add(r);
                    routeIterator.remove();
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeFilter(String string) {
        for (Iterator<Displayable> routeIterator = filtered.iterator(); routeIterator.hasNext(); ) {
            Displayable r = routeIterator.next();
            if (r instanceof Route) {
                Route route = (Route) r;
                if (route.getType().toLowerCase().contains(string.toLowerCase())) {
                    displayables.add(r);
                    routeIterator.remove();
                }
            }
        }
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
                inflate(R.layout.search_result_row_route, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Displayable displayable = (Displayable) displayables.get(position);
        holder.rect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.hasConnection()) {
                    new CheckForRouteUpdateTask(displayable, activity).execute();
                }
                activity.launch(displayable);
            }
        });
        if (displayable instanceof Route) {
            Route r = (Route) displayable;
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
            Area a = (Area) displayable;
            holder.text1.setText(a.getName());
            List<Displayable> within = LocalDatabase.getInstance(activity).findRoutesWithin(a);
            holder.text2.setText(within.size() + " displayables");
            double average = 0;
            int size = 0;
            for (Displayable i : within) {
                Route route = (Route) i;
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
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        LinearLayout rect;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.list_row_one);
            text2 = (TextView) itemView.findViewById(R.id.list_row_two);
            text3 = (TextView) itemView.findViewById(R.id.list_row_three);
            text4 = (TextView) itemView.findViewById(R.id.list_row_four);
            rect = (LinearLayout) itemView.findViewById(R.id.rectangle);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
