package com.cragchat.mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.sql.CheckForRouteUpdateTask;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.List;

public class RouteListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private List<Route> routes;
    private static LayoutInflater inflater;
    private CragChatActivity activity;

    public RouteListAdapter(CragChatActivity a, List<Route> routes) {
        this.routes = routes;
        inflater = a.getLayoutInflater();
        this.activity = a;
        stripe = true;
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public Object getItem(int position) {
        return routes.get(position);
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
        LinearLayout rect;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Displayable a = LocalDatabase.getInstance(activity).findExact(routes.get(position).getId());
        if (activity.hasConnection()) {
            //Log.d("RemoteDatabase", "Connected to internet - checking revision");
            new CheckForRouteUpdateTask(a, activity).execute();
        }
        activity.launch(a);
    }

    private boolean stripe;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            vi = inflater.inflate(R.layout.search_result_row_route, parent, false);
            holder.text1 = (TextView) vi.findViewById(R.id.list_row_one);
            holder.text2 = (TextView) vi.findViewById(R.id.list_row_two);
            holder.text3 = (TextView) vi.findViewById(R.id.list_row_three);
            holder.text4 = (TextView) vi.findViewById(R.id.list_row_four);
            holder.rect = (LinearLayout) vi.findViewById(R.id.rectangle);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        //holder.rect.setBackgroundColor(position %2 == 0 ? Color.TRANSPARENT: Color.LTGRAY);
        Route r = routes.get(position);
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

        return vi;
    }
}
