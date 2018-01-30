package com.cragchat.mobile.ui.view.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.presenter.RecentActivityFragmentPresenter;
import com.cragchat.mobile.ui.view.RecentActivityView;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.ui.view.adapters.recycler.RecentActivityRecyclerAdapter;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.cragchat.mobile.util.NavigationUtil.ENTITY_KEY;

public class RecentActivityFragment extends BaseFragment implements RecentActivityView {

    @BindView(R.id.progressBar1)
    ProgressBar progressBar;
    @BindView(R.id.empty_text)
    TextView empty;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject RecentActivityFragmentPresenter mPresenter;

    @Inject RecentActivityRecyclerAdapter adapter;

    public static RecentActivityFragment newInstance(Area area) {
        RecentActivityFragment f = new RecentActivityFragment();
        Bundle b = new Bundle();
        b.putParcelable(ENTITY_KEY, area);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DaggerRecentActivityComponent component = DaggerRecentActivityComponent.builder();
        View view = inflater.inflate(R.layout.fragment_recent_activity, container, false);
        ButterKnife.bind(this, view);
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
        return view;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.setView(this);
        if (savedInstanceState == null) {
            mPresenter.loadRecentActivity();
        }
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
