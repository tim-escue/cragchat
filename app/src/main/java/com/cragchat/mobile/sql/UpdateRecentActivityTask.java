package com.cragchat.mobile.sql;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.adapters.recycler.RecentActivityRecyclerAdapter;
import com.cragchat.mobile.descriptor.Datable;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.remote.ResponseHandler;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class UpdateRecentActivityTask extends AsyncTask<Void, Integer, List<String>> {

    private long cur;
    private Activity con;
    private View bar;
    private RecyclerView view;
    private int displayId;

    public UpdateRecentActivityTask(Activity con, int displayId, RecyclerView view, View bar) {
        cur = System.currentTimeMillis();
        this.bar = bar;
        this.con = con;
        this.displayId = displayId;
        this.view = view;
    }

    protected List<String> doInBackground(Void... urls) {
        return RemoteDatabase.getRecentActivity(displayId);
    }

    protected void onPostExecute(List<String> feed) {
        if (feed != null) {
            List<Object> haha = new LinkedList<>();
            for (String i : feed) {
                Object j = ResponseHandler.parseResponse(con, i);
                if (j != null) {
                    haha.add(j);
                }
            }
            Collections.sort(haha, comparator);
            bar.setVisibility(View.GONE);
            // final RecentActivityAdapter adapter = new RecentActivityAdapter((CragChatActivity)con, haha);
            view.setAdapter(new RecentActivityRecyclerAdapter((CragChatActivity) con, haha));
            view.setVisibility(View.VISIBLE);
            view.getAdapter().notifyDataSetChanged();
        } else {
            bar.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            TextView tvv = (TextView) con.findViewById(R.id.empty_text);
            if (!((CragChatActivity) con).hasConnection()) {
                tvv.setText("Internet connection required to load recent activity");
                tvv.setVisibility(View.VISIBLE);
            } else {
                tvv.setText("No recent activity");
                tvv.setVisibility(View.VISIBLE);
            }
        }
    }

    public Comparator<Object> comparator = new Comparator<Object>() {
        @Override
        public int compare(Object lhs, Object rhs) {
            return ((Datable) rhs).getDate().compareTo(((Datable) lhs).getDate());
        }
    };
}