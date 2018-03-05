package com.cragchat.mobile.mvp.presenter;

import com.cragchat.mobile.data.Repository;
import com.cragchat.mobile.mvp.contract.SearchContract;

import javax.inject.Inject;

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
