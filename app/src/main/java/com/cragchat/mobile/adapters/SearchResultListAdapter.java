package com.cragchat.mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import java.util.List;

public class SearchResultListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private Displayable[] results;
    private static LayoutInflater inflater;
    private CragChatActivity activity;

    public SearchResultListAdapter(CragChatActivity a, Displayable[] results) {
        this.results = results;
        inflater = a.getLayoutInflater();
        activity = a;
    }

    @Override
    public int getCount() {
        return results.length;
    }

    @Override
    public Object getItem(int position) {
        return results[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        ImageView icon;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Displayable target = LocalDatabase.getInstance(activity).findExact(results[position].getId());
        if (activity.hasConnection()) {
            //Log.d("RemoteDatabase", "Connected to internet - checking revision");
            new CheckForRouteUpdateTask(target, activity).execute();
        }
        activity.launch(target);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        boolean route = results[position] instanceof Route;
        if (convertView == null) {
            holder = new ViewHolder();
            vi = inflater.inflate(R.layout.search_result_row_route, parent, false);
            holder.text1 = (TextView) vi.findViewById(R.id.list_row_one);
            holder.text2 = (TextView) vi.findViewById(R.id.list_row_two);
            holder.text3 = (TextView) vi.findViewById(R.id.list_row_three);
            holder.text4 = (TextView) vi.findViewById(R.id.list_row_four);
            holder.icon = vi.findViewById(R.id.icon);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        if (route) {
            Route r = (Route) results[position];
            holder.text1.setText(r.getName());
            String sters;
            String yds;
            if (r.getYds(activity) == -1) {
                yds = "Not rated";
                sters = "Not rated";
            } else {
                yds = r.getYdsString(activity, r.getYds(activity));
                sters = r.getStarsString(activity) + " stars";
            }
            holder.text2.setText(yds);
            holder.text3.setText(sters);
            holder.text4.setVisibility(View.VISIBLE);
            holder.text4.setText(r.getType());
            holder.icon.setImageResource(r.getType().equalsIgnoreCase("sport") ? R.drawable.bolt_img : R.drawable.nuts);
        } else {
            Area a = (Area) results[position];
            holder.text1.setText(a.getName());
            List<Route> within = LocalDatabase.getInstance(activity).findRoutesWithin(a);
            holder.text2.setText(within.size() + " routes");
            double average = 0;
            int size = 0;
            for (Route i : within) {
                if (i.getYds(activity) != -1) {
                    average += i.getStars(activity);
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
        }
        return vi;

    }
}
