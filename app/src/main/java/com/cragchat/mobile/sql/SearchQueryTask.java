package com.cragchat.mobile.sql;

import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.adapters.SearchResultListAdapter;
import com.cragchat.mobile.descriptor.Displayable;

import java.util.List;

public class SearchQueryTask extends AsyncTask<String, Integer, List<Displayable>> {

    private CragChatActivity context;
    private ListView view;

    public SearchQueryTask(CragChatActivity context, ListView view) {
        this.context = context;
        this.view = view;
    }

    private String search;

    protected List<Displayable> doInBackground(String... urls) {
        search = urls[0];
        return LocalDatabase.getInstance(context).findAllWithName(search);
    }

    protected void onPostExecute(List<Displayable> feed) {
        if (feed.size()> 0) {
            SearchResultListAdapter adapter = new SearchResultListAdapter(context, feed.toArray(new Displayable[feed.size()]));
            view.setAdapter(adapter);
            view.setOnItemClickListener(adapter);
            adapter.notifyDataSetChanged();
        } else {
            TextView empty = (TextView) context.findViewById(R.id.list_empty);
            if (empty != null) {
                empty.setText("No results found for \"" + search + "\"\n\n" +
                        "Try another name");
                view.setEmptyView(empty);
            }
        }
    }
}