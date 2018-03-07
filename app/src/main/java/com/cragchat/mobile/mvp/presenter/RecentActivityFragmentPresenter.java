package com.cragchat.mobile.mvp.presenter;

import com.cragchat.mobile.data.Repository;
import com.cragchat.mobile.domain.model.Area;
import com.cragchat.mobile.domain.model.Datable;
import com.cragchat.mobile.mvp.view.RecentActivityView;
import com.cragchat.mobile.domain.util.FormatUtil;

import java.util.List;

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
                    FormatUtil.transformList(mArea.getSubAreas(), area -> area.toString()),
                    FormatUtil.transformList(mArea.getRoutes(), route -> route.toString()))
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