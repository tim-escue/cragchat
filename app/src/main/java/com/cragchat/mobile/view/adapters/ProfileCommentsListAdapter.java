package com.cragchat.mobile.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.comments.ProfileCommentManager;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.sql.LocalDatabase;

public class ProfileCommentsListAdapter extends BaseAdapter {

    private CragChatActivity activity;
    private ProfileCommentManager manager;
    private static LayoutInflater inflater;

    public ProfileCommentsListAdapter(Activity a, ProfileCommentManager manager) {
        activity = (CragChatActivity) a;
        this.manager = manager;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return manager.getCommentList().size();
    }

    @Override
    public Object getItem(int position) {
        return manager.getCommentList().get(position);
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
        TextView text5;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.comment_list_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text1 = (TextView) vi.findViewById(R.id.comment_author);
            holder.text2 = (TextView) vi.findViewById(R.id.comment_date);
            holder.text3 = (TextView) vi.findViewById(R.id.comment_body);
            holder.text4 = (TextView) vi.findViewById(R.id.reply);
            holder.text4.setVisibility(View.GONE);
            holder.text5 = (TextView) vi.findViewById(R.id.text_score);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();


        final Comment comment = manager.getCommentList().get(position);

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Displayable disp = LocalDatabase.getInstance(activity).findExact(comment.getDisplayId());

                if (disp instanceof Route) {
                    int i = 0;
                    if (comment.getTable().equals("DISCUSSION")) {
                        i = 1;
                    } else if (comment.getTable().equals("LOCATION")) {
                        i = 3;
                    }
                    activity.launch(disp, i);
                } else if (disp instanceof Area) {
                    int i = 0;
                    if (comment.getTable().equals("DISCUSSION")) {
                        i = 1;
                    } else if (comment.getTable().equals("LOCATION")) {
                        i = 3;
                    }
                    activity.launch(disp, 1);
                }

            }
        });

        holder.text5.setText(String.valueOf(manager.getCommentList().get(position).getScore()));
        holder.text1.setText("Posted in " + comment.getTable().toLowerCase() + " for " + LocalDatabase.getInstance(activity).findExact(comment.getDisplayId()).getName());
        holder.text2.setText(comment.getDate());
        holder.text3.setText(comment.getText());

        holder.text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return vi;

    }
}
