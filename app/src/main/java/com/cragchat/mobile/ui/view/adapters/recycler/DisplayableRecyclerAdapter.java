package com.cragchat.mobile.ui.view.adapters.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.util.NavigationUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayableRecyclerAdapter extends RecyclerView.Adapter<DisplayableRecyclerAdapter.ViewHolder> {

    private List<Object> displayables;
    private CragChatActivity activity;
    private List<Object> filtered;

    public DisplayableRecyclerAdapter(CragChatActivity a, List<Object> routes) {
        this.displayables = routes;
        this.activity = a;
        filtered = new ArrayList<>();
    }

    public void setDisplayables(List<Object> list) {
        this.displayables = list;
        notifyDataSetChanged();
    }

    public void addFilter(String string) {
        for (Iterator<Object> routeIterator = displayables.iterator(); routeIterator.hasNext(); ) {
            Object r = routeIterator.next();
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
        for (Iterator<Object> routeIterator = filtered.iterator(); routeIterator.hasNext(); ) {
            Object r = routeIterator.next();
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
        final Object displayable = displayables.get(position);
        if (displayable instanceof Area) {

            holder.rect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationUtil.launch(activity, (Area) displayable);
                }

            });
        }
        if (displayable instanceof Route) {
            Route r = (Route) displayable;
            holder.text1.setText(r.getName());
            StringBuilder info = new StringBuilder();
            if (r.getYds() == -1) {
                info.append("Not rated");
            } else {
                info.append(r.getYds());
                info.append(" ");
                info.append(r.getStars());
                info.append(" stars");
            }
            holder.text2.setText(info.toString());
            holder.icon.setImageResource(r.getType().equalsIgnoreCase("sport") ? R.drawable.bolt_img : R.drawable.nuts);
        } else {
            Area a = (Area) displayable;
            holder.text1.setText(a.getName());
            List<String> routes = a.getRoutes();
            StringBuilder info = new StringBuilder();
            info.append(routes.size());
            info.append(" routes");
            List<String> subAreas = a.getSubAreas();
            if (subAreas.size() > 0) {
                info.append(" ");
                info.append(subAreas.size());
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

        @BindView(R.id.author)
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
