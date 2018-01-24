package com.cragchat.mobile.ui.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.view.activity.SearchActivity;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.ui.view.adapters.recycler.SearchResultsRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by timde on 1/6/2018.
 */

public class SearchActivityPresenter {

    @BindView(R.id.results_recycler_view)
    RecyclerView resultsRecycler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SearchActivity activity;

    public SearchActivityPresenter(SearchActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setTitle("Results");
    }

    public void present(String query, Repository repository) {
        activity.getSupportActionBar().setSubtitle("\"" + query + "\"");
        List queryResults = repository.getQueryMatches(query, null);
        RecyclerUtils.setAdapterAndManager(resultsRecycler,
                new SearchResultsRecyclerAdapter(activity, queryResults),
                LinearLayoutManager.VERTICAL);
    }
}
