package com.cragchat.mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.R;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.List;

public class AreaListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private CragChatActivity activity;
    private List<Area> routes;
    private static LayoutInflater inflater;

    public AreaListAdapter(CragChatActivity a, List<Area> routes) {
        activity = a;
        this.routes = routes;
        inflater = activity.getLayoutInflater();
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Displayable a = LocalDatabase.getInstance(activity).findExact(routes.get(position).getId());
        activity.launch(a);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.layout_list_row_wall, null);
            holder = new ViewHolder();
            holder.text1 = (TextView) vi.findViewById(R.id.list_row_one);
            holder.text2 = (TextView) vi.findViewById(R.id.list_row_two);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        if (routes.size() == 0 ) {
            holder.text1.setText("No further areas");
        } else {
            Area r = routes.get(position);
            holder.text1.setText(r.getName());
            holder.text2.setText(String.valueOf(LocalDatabase.getInstance(activity).findRoutesWithin(r).size()) + " routes");
        }
        return vi;

    }
}
