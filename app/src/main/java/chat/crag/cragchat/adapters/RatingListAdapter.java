package chat.crag.cragchat.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import chat.crag.cragchat.CragChatActivity;
import chat.crag.cragchat.ProfileActivity;
import chat.crag.cragchat.R;
import chat.crag.cragchat.descriptor.Area;
import chat.crag.cragchat.descriptor.Displayable;
import chat.crag.cragchat.descriptor.Rating;
import chat.crag.cragchat.descriptor.Route;
import chat.crag.cragchat.search.SearchableActivity;
import chat.crag.cragchat.sql.LocalDatabase;

import java.util.List;

public class RatingListAdapter extends BaseAdapter {

    private CragChatActivity activity;
    private static LayoutInflater inflater;
    private List<Rating> ratings;
    private boolean forProfile;

    public RatingListAdapter(Activity a, List<Rating> ratings, boolean forProfile) {
        activity = (CragChatActivity) a;
        inflater = activity.getLayoutInflater();
        this.ratings = ratings;
        this.forProfile = forProfile;
    }

    @Override
    public int getCount() {
        return ratings.size();
    }

    @Override
    public Object getItem(int position) {
        return ratings.get(position);
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
        //TextView text9;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_list_rating, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text1 = (TextView) vi.findViewById(R.id.item_rating_username);
            holder.text2 = (TextView) vi.findViewById(R.id.item_rating_date);
            holder.text3 = (TextView) vi.findViewById(R.id.item_rating_yds);
            holder.text4 = (TextView) vi.findViewById(R.id.item_rating_stars);
            //  holder.text9 = (TextView) vi.findViewById(R.id.item_rating_time);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        final Rating rating = ratings.get(position);
        String title;
        if (forProfile) {
            title = LocalDatabase.getInstance(activity).findExact(rating.getRouteId()).getName();
            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Displayable disp = LocalDatabase.getInstance(activity).findExact(rating.getRouteId());
                    activity.launch(disp, 2);
                }
            });
        } else {
            title = rating.getUserName();
        }
        holder.text1.setText(title);
        holder.text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.hasConnection()) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("username", rating.getUserName());
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Must have internet connection to view user profiles.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.text2.setText(rating.getDate());
        holder.text3.setText("Yds: " + activity.getResources().getStringArray(R.array.yds_options)[rating.getYds()]);
        holder.text4.setText("Stars: " + String.valueOf(rating.getStars()));
        return vi;

    }
}
