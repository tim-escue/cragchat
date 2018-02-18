package com.cragchat.mobile.ui.view.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.presenter.RecentActivityFragmentPresenter;
import com.cragchat.mobile.ui.view.RecentActivityView;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.ui.view.adapters.recycler.RecentActivityRecyclerAdapter;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;


public class RecentActivityFragment extends DaggerFragment implements RecentActivityView {

    @BindView(R.id.progressBar1)
    ProgressBar progressBar;
    @BindView(R.id.empty_text)
    TextView empty;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    RecentActivityFragmentPresenter mPresenter;

    @Inject
    Area area;

    @Inject
    Repository repository;

    RecentActivityRecyclerAdapter adapter;

    @Inject
    public RecentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_recent_activity, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new RecentActivityFragmentPresenter(repository, area);
        adapter = new RecentActivityRecyclerAdapter(getContext(), area.getKey(), repository);
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
        mPresenter.setView(this);
        mPresenter.loadRecentActivity();
        return view;
    }

    @Override
    public void showList(List<Datable> recentActivity) {
        adapter.update(recentActivity);
        empty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.material_background));
    }

    @Override
    public void hideList() {
        empty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
