package chat.crag.cragchat;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import chat.crag.cragchat.search.SearchableActivity;
import chat.crag.cragchat.sql.SearchQueryTask;

public class SearchActivity extends SearchableActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new SearchQueryTask(this).execute(query);
        }
    }


}
