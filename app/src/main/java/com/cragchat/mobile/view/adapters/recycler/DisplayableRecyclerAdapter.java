package com.cragchat.mobile.view.adapters.recycler;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                inflate(R.layout.displayable_recycler_row, parent, false);
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
            StringBuilder info = new StringBuilder();
            if (r.getYds(activity) == -1) {
                info.append("Not rated");
            } else {
                info.append(Displayable.getYdsString(activity, r.getYds(activity)));
                info.append(" ");
                info.append(r.getStarsString(activity));
                info.append(" stars");
            }
            holder.text2.setText(info.toString());
            holder.icon.setImageResource(r.getType().equalsIgnoreCase("sport") ? R.drawable.bolt_img : R.drawable.nuts);
        } else {
            Area a = (Area) displayable;
            holder.text1.setText(a.getName());
            List<? extends Displayable> within = LocalDatabase.getInstance(activity).findRoutesWithin(a);
            StringBuilder info = new StringBuilder();
            info.append(within.size());
            info.append(" routes");
            within = LocalDatabase.getInstance(activity).findAreasWithin(a);
            if (within.size() > 0) {
                info.append(" ");
                info.append(within.size());
                info.append(" areas");
            }
            holder.text2.setText(info.toString());
            /*double average = 0;
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
            }*/
            holder.icon.setImageResource(R.drawable.area_mountain);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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
    }
}
