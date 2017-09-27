package com.cragchat.mobile.sql;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.view.adapters.recycler.SearchResultRecyclerAdapter;
import com.cragchat.mobile.descriptor.Displayable;

import java.util.List;

public class SearchQueryTaskNew extends AsyncTask<String, Integer, List<Displayable>> {

    private CragChatActivity context;
    private RecyclerView view;

    public SearchQueryTaskNew(CragChatActivity context, RecyclerView view) {
        this.context = context;
        this.view = view;
    }

    private String search;

    protected List<Displayable> doInBackground(String... urls) {
        search = urls[0];
        return LocalDatabase.getInstance(context).findAllWithName(search);
    }

    protected void onPostExecute(List<Displayable> feed) {
        SearchResultRecyclerAdapter adapter = (SearchResultRecyclerAdapter) view.getAdapter();
        adapter.setResults(feed);
        adapter.notifyDataSetChanged();
    }
}