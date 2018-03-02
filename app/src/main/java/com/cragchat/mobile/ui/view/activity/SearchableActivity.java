package com.cragchat.mobile.ui.view.activity;

import android.app.SearchManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.ui.view.adapters.recycler.SearchResultsRecyclerAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by timde on 11/9/2017.
 */

public abstract class SearchableActivity extends CragChatActivity {

    public abstract int getToolbarColor();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        final View filter = findViewById(R.id.dark_filter);
        final View toolbar = findViewById(R.id.toolbar);
        final RecyclerView recyclerView = findViewById(R.id.search_results_recycler);

        final SearchResultsRecyclerAdapter adapter = new SearchResultsRecyclerAdapter(this,
                null);
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                List data;
                if (s.isEmpty()) {
                    data = Collections.emptyList();
                } else {
                    data = repository.getQueryMatches(s,
                            new Callback<List>() {
                                @Override
                                public void onSuccess(List object) {
                                    if (searchView != null && searchView.getQuery() != null &&
                                            searchView.getQuery().equals(s)) {
                                        adapter.update(object);
                                    }
                                }
                            });
                }
                adapter.update(data);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter.setVisibility(View.VISIBLE);
                toolbar.setBackgroundColor(getResources().getColor(R.color.primary));
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    searchItem.collapseActionView();
                    searchView.setQuery("", false);
                    filter.setVisibility(View.GONE);
                    toolbar.setBackgroundColor(getToolbarColor());
                }
            }
        });

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

}
