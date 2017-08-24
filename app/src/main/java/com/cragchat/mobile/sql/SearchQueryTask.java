package com.cragchat.mobile.sql;

import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.SearchResultListAdapter;
import com.cragchat.mobile.descriptor.Displayable;

public class SearchQueryTask extends AsyncTask<String, Integer, Displayable[]> {

    private CragChatActivity context;

    public SearchQueryTask(CragChatActivity context) {
        this.context = context;
    }

    private String search;

    protected Displayable[] doInBackground(String... urls) {
        search = urls[0];
        return LocalDatabase.getInstance(context).findAllWithName(search);
    }

    protected void onPostExecute(Displayable[] feed) {
        ListView lv = (ListView) context.findViewById(R.id.listView);
        if (feed.length > 0) {
            SearchResultListAdapter adapter = new SearchResultListAdapter(context, feed);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(adapter);
            adapter.notifyDataSetChanged();
        } else {
            TextView empty = (TextView) context.findViewById(R.id.list_empty);
            empty.setText("No results found for \"" + search +"\"\n\n" +
                    "Try another name");
            lv.setEmptyView(empty);
        }
    }
}