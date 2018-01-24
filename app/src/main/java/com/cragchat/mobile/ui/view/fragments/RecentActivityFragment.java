package com.cragchat.mobile.ui.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.presenter.RecentActivityFragmentPresenter;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cragchat.mobile.util.NavigationUtil.ENTITY_KEY;

public class RecentActivityFragment extends BaseFragment {

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

        final RecentActivityFragmentPresenter presenter =
                new RecentActivityFragmentPresenter((CragChatActivity) getActivity(), view, area,
                        repository);

        List<Datable> recentActivity = repository.getRecentActivity(area.getKey(),
                area.getSubAreas(),
                area.getRoutes(),
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

}
