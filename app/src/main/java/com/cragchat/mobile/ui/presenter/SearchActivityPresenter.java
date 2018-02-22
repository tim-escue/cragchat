package com.cragchat.mobile.ui.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.contract.SearchContract;
import com.cragchat.mobile.ui.view.activity.SearchActivity;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.ui.view.adapters.recycler.SearchResultsRecyclerAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by timde on 1/6/2018.
 */

public class SearchActivityPresenter implements SearchContract.SearchPresenter {

    private SearchContract.SearchView activity;
    private Repository repository;

    @Inject
    public SearchActivityPresenter(SearchContract.SearchView activity, Repository repository) {
        this.activity = activity;
        this.repository = repository;
    }


    @Override
    public void loadSearchResults(String query) {
        activity.show(repository.getQueryMatches(query, null));
    }
}
