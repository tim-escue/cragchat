package com.cragchat.mobile.fragments;

import android.content.res.Resources;
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
    @BindView(R.id.progressBar1)
    ProgressBar progressBar;

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
        View view = inflater.inflate(R.layout.fragment_recent_activity, container, false);
        ButterKnife.bind(this, view);

        Area area = getArguments().getParcelable(ENTITY_KEY);

        final RecentActivityFragmentPresenter presenter = new RecentActivityFragmentPresenter(view, area);

        List<Datable> recentActivity = Repository.getRecentActivity(area.getKey(),
                area != null ? area.getSubAreas() : null,
                area != null ? area.getRoutes() : null,
                new Callback<List<Datable>>() {
                    @Override
                    public void onSuccess(List<Datable> object) {
                        presenter.present(object);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
        presenter.present(recentActivity);

        return view;
    }

    class RecentActivityFragmentPresenter {

        RecentActivityRecyclerAdapter adapter;
        @BindView(R.id.empty_text)
        TextView empty;
        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;

        public RecentActivityFragmentPresenter(View parent, Area area) {
            ButterKnife.bind(this, parent);
            recyclerView.setHasFixedSize(true);
            adapter = new RecentActivityRecyclerAdapter(
                    (CragChatActivity) getActivity(), area.getKey(), null);

            RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
        }

        public void present(List<Datable> recentActivity) {
            Collections.sort(recentActivity, new Comparator<Datable>() {
                @Override
                public int compare(Datable datable, Datable t1) {
                    return t1.getDate().compareTo(datable.getDate());
                }
            });
            Resources resources = recyclerView.getContext().getResources();
            if (recentActivity.isEmpty()) {
                empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                recyclerView.setBackgroundColor(resources.getColor(R.color.cardview_light_background));
            } else {
                adapter.update(recentActivity);
                empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setBackgroundColor(resources.getColor(R.color.material_background));
            }
        }
    }

}
