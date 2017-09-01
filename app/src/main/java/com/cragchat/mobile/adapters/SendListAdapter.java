package com.cragchat.mobile.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.ProfileActivity;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Send;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.util.FormatUtil;

import java.util.List;

public class SendListAdapter extends BaseAdapter {

    private CragChatActivity activity;
    private static LayoutInflater inflater;
    private List<Send> sends;
    private boolean forProfile;

    public SendListAdapter(Activity a, List<Send> sends, boolean forProfile) {
        activity = (CragChatActivity) a;
        inflater = activity.getLayoutInflater();
        this.sends = sends;
        this.forProfile = forProfile;
    }

    @Override
    public int getCount() {
        return sends.size();
    }

    @Override
    public Object getItem(int position) {
        return sends.get(position);
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
        TextView text6;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_list_send, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text1 = (TextView) vi.findViewById(R.id.item_send_username);
            holder.text2 = (TextView) vi.findViewById(R.id.item_send_date);
            holder.text3 = (TextView) vi.findViewById(R.id.item_send_pitches);
            holder.text4 = (TextView) vi.findViewById(R.id.item_send_attempts);
            holder.text5 = (TextView) vi.findViewById(R.id.item_send_style);
            holder.text6 = (TextView) vi.findViewById(R.id.item_send_send_type);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        final Send send = sends.get(position);
        String title;
        if (forProfile) {
            title = LocalDatabase.getInstance(activity).findExact(send.getRouteId()).getName();
            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Displayable disp = LocalDatabase.getInstance(activity).findExact(send.getRouteId());
                    activity.launch(disp, 2);
                }
            });
        } else {
            title = send.getUserName();
        }
        holder.text1.setText(title);
        holder.text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.hasConnection()) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("username", send.getUserName());
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Must have internet connection to view user profiles.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.text2.setText("Submitted on " + FormatUtil.getFormattedDate(send.getDate()));
        holder.text3.setText("Pitches: " + send.getPitches());
        holder.text4.setText("Attempts: " + send.getAttempts());
        holder.text5.setText("Style: " + send.getStyle());
        holder.text6.setText("Send Type: " + send.getSendType());

        return vi;

    }
}
