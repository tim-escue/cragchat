package com.cragchat.mobile.ui.presenter;

import android.util.Log;

import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.view.RecentActivityView;
import com.cragchat.mobile.util.FormatUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class RecentActivityFragmentPresenter {

    private RecentActivityView mView;
    private Area mArea;
    private Repository mRepository;

    public RecentActivityFragmentPresenter(Repository repository, Area area) {
        this.mRepository = repository;
        this.mArea = area;
    }

    public void setView(RecentActivityView view) {
        this.mView = view;
    }

    public void loadRecentActivity() {

        mRepository.observeRecentActivity(mArea.getKey(),
                    FormatUtil.transform(mArea.getSubAreas(), area -> area.toString()),
                    FormatUtil.transform(mArea.getRoutes(), route -> route.toString()))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recentActivityObserver);
    }

    private Observer<List<Datable>> recentActivityObserver = new Observer<List<Datable>>() {

        boolean displayed;

        @Override
        public void onSubscribe(Disposable disposable) {

        }

        @Override
        public void onNext(List<Datable> datables) {
            if (!datables.isEmpty()) {
                mView.showList(datables);
                displayed = true;
            }
        }

        @Override
        public void onError(Throwable throwable) {
            mView.hideProgressBar();
            if (!displayed) {
                mView.hideList();
            }
        }

        @Override
        public void onComplete() {
            mView.hideProgressBar();
            if (!displayed) {
                mView.hideList();
            }
        }
    };

}