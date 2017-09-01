package com.cragchat.mobile.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.search.NavigableActivity;
import com.cragchat.mobile.sql.SearchQueryTask;

public class SearchActivity extends NavigableActivity {

    ListView resultsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Results");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleIntent(getIntent());

        this.resultsList = findViewById(R.id.listView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new SearchQueryTask(this, resultsList).execute(query);
        }
    }


}
