package com.cragchat.mobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Datable;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.view.adapters.recycler.RecentActivityRecyclerAdapter;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentActivityFragment extends Fragment {

    private static final String ENTITY_KEY = "entityKey";

    RecentActivityRecyclerAdapter adapter;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static RecentActivityFragment newInstance(String entityKey) {
        RecentActivityFragment f = new RecentActivityFragment();
        Bundle b = new Bundle();
        b.putString(ENTITY_KEY, entityKey);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_recent_activity, container, false);
        ButterKnife.bind(this, view);

        String entityKey;
        entityKey = getArguments().getString(ENTITY_KEY);
        Area area = Repository.getArea(entityKey, null);

        recyclerView.setHasFixedSize(true);

        adapter = new RecentActivityRecyclerAdapter(
                (CragChatActivity) getActivity(), null);
        List<Datable> recentActivity = Repository.getRecentActivity(entityKey,
                area != null ? area.getSubAreas() : null,
                area != null ? area.getRoutes() : null,
                new Callback<List<Datable>>() {
                    @Override
                    public void onSuccess(List<Datable> object) {
                        bind(object);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure() {
                        progressBar.setVisibility(View.GONE);
                    }
                });

        Collections.sort(recentActivity, new Comparator<Datable>() {
            @Override
            public int compare(Datable datable, Datable t1) {
                return t1.getDate().compareTo(datable.getDate());
            }
        });

        bind(recentActivity);

        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);

        return view;
    }

    private void bind(List<Datable> data) {
        if (data.size() > 0) {
            Collections.sort(data, new Comparator<Datable>() {
                @Override
                public int compare(Datable datable, Datable t1) {
                    return t1.getDate().compareTo(datable.getDate());
                }
            });
            adapter.update(data);

        }
    }

}
