package com.cragchat.mobile.ui.presenter;

import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.ui.view.adapters.recycler.RecentActivityRecyclerAdapter;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentActivityFragmentPresenter {

    RecentActivityRecyclerAdapter adapter;
    @BindView(R.id.empty_text)
    TextView empty;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public RecentActivityFragmentPresenter(CragChatActivity activity, View parent, Area area, Repository repository) {
        ButterKnife.bind(this, parent);
        recyclerView.setHasFixedSize(true);
        adapter = new RecentActivityRecyclerAdapter(activity, area.getKey(), null, repository);

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