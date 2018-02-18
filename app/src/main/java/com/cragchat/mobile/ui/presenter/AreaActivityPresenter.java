package com.cragchat.mobile.ui.presenter;

import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.contract.AreaContract;
import com.cragchat.mobile.ui.view.fragments.RecentActivityFragment;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.view.activity.AreaActivity;
import com.cragchat.mobile.ui.view.fragments.AreaListFragment;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by timde on 1/4/2018.
 */

public class AreaActivityPresenter implements AreaContract.AreaPresenter {

    private AreaContract.AreaView view;
    private Repository repository;

    @Inject
    public AreaActivityPresenter(AreaContract.AreaView view, Repository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void updateArea(String areaKey) {
        repository.observeArea(areaKey).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private final Observer<Area> observer = new Observer<Area>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Area value) {
            view.present(value);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };
}
