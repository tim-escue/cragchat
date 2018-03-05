package com.cragchat.mobile.mvp.view.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cragchat.mobile.R;
import com.cragchat.mobile.mvp.contract.SearchContract;
import com.cragchat.mobile.mvp.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.mvp.view.adapters.recycler.SearchResultsRecyclerAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by timde on 10/30/2017.
 */

public class SearchActivity extends SearchableActivity implements SearchContract.SearchView {

    @BindView(R.id.results_recycler_view)
    RecyclerView resultsRecycler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    SearchContract.SearchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Results");

        Intent intent = getIntent();
        String query = intent.getStringExtra(SearchManager.QUERY);
        getSupportActionBar().setSubtitle("\"" + query + "\"");
        presenter.loadSearchResults(query);

    }

    public void show(List queryResults) {
        RecyclerUtils.setAdapterAndManager(resultsRecycler,
                new SearchResultsRecyclerAdapter(this, queryResults),
                LinearLayoutManager.VERTICAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}
