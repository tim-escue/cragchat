package com.cragchat.mobile.di.module;

import com.cragchat.mobile.data.Repository;
import com.cragchat.mobile.mvp.contract.SearchContract;
import com.cragchat.mobile.mvp.presenter.SearchActivityPresenter;
import com.cragchat.mobile.mvp.view.activity.SearchActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by timde on 2/14/2018.
 */

@Module
public class SearchModule {

    @Provides
    SearchContract.SearchPresenter presenter(SearchActivity activity, Repository repository) {
        return new SearchActivityPresenter(activity, repository);
    }
}
