package com.cragchat.mobile.ui.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.view.RecentActivityView;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.ui.view.adapters.recycler.RecentActivityRecyclerAdapter;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RecentActivityFragmentPresenter {

    private RecentActivityView mView;
    private Area mArea;
    private Repository mRepository;

    @Inject
    public RecentActivityFragmentPresenter(Repository repository, Area area) {
        this.mRepository = repository;
        this.mArea = area;
    }

    public void setView(RecentActivityView view) {
        this.mView = view;
    }

    public void loadRecentActivity() {
        mRepository.recentActivity(mArea.getKey(), mArea.getSubAreas(), mArea.getRoutes())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(recentActivityObserver());
    }

    private Observer<List<Datable>> recentActivityObserver() {
        return new Observer<List<Datable>>() {

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
}