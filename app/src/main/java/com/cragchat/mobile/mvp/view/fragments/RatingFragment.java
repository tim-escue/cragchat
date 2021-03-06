package com.cragchat.mobile.mvp.view.fragments;


import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.data.authentication.Authentication;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.data.Repository;
import com.cragchat.mobile.domain.model.Rating;
import com.cragchat.mobile.mvp.view.activity.CragChatActivity;
import com.cragchat.mobile.mvp.view.activity.RateRouteActivity;
import com.cragchat.mobile.mvp.view.adapters.recycler.RatingRecyclerAdapter;
import com.cragchat.mobile.mvp.view.adapters.recycler.RecyclerUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;


public class RatingFragment extends DaggerFragment implements View.OnClickListener {

    private RatingFragmentPresenter presenter;

    @Inject
    @Named(InjectionNames.ENTITY_KEY)
    String entityKey;

    @Inject
    Repository mRepository;

    @Inject
    Authentication mAuthentication;

    @Inject
    public RatingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_ratings, container, false);

        presenter = new RatingFragmentPresenter(view, getLifecycle());
        mRepository.observeRatings(entityKey).subscribe(ratings -> presenter.present(ratings));

        return view;
    }

    class RatingFragmentPresenter {

        @BindView(R.id.list_ratings)
        RecyclerView recyclerView;
        @BindView(R.id.list_empty)
        TextView empty;
        private RatingRecyclerAdapter adapter;

        public RatingFragmentPresenter(View parent, Lifecycle lifecycle) {
            ButterKnife.bind(this, parent);
            adapter = RatingRecyclerAdapter.create(entityKey, (CragChatActivity) getActivity());

            RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
            lifecycle.addObserver(adapter);
        }

        public void present(List<Rating> ratings) {
            Resources resources = recyclerView.getContext().getResources();
            if (ratings.isEmpty()) {
                empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                recyclerView.setBackgroundColor(resources.getColor(R.color.cardview_light_background));
            } else {
                empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setBackgroundColor(resources.getColor(R.color.material_background));
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (mAuthentication.isLoggedIn(view.getContext())) {
                Intent intent = new Intent(view.getContext(), RateRouteActivity.class);
                intent.putExtra("entityKey", entityKey);
                view.getContext().startActivity(intent);
            } else {
                //Log.d("RouteActivity", "Must be logged in to add an image");
                DialogFragment df = NotificationDialog.newInstance("Must be logged in to rate a route.");
                df.show(getFragmentManager(), "dialog");
            }
        }
    }
}
