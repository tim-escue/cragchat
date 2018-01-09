package com.cragchat.mobile.ui.view.fragments;


import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Rating;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.ui.view.activity.RateRouteActivity;
import com.cragchat.mobile.ui.view.adapters.recycler.RatingRecyclerAdapter;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RatingFragment extends Fragment implements View.OnClickListener {

    private RatingFragmentPresenter presenter;
    private String entityKey;

    public static RatingFragment newInstance(String displayableId) {
        RatingFragment f = new RatingFragment();
        Bundle b = new Bundle();
        b.putString("entityKey", displayableId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_ratings, container, false);
        entityKey = getArguments().getString("entityKey");

        /*
            Called only to update ratings. The return value is not used because RatingRecyclerAdapter
            relies upon an instance of Realm and Reposity can not expose Realm API
            in order to preserve CLEAN architecture.
         */
        presenter = new RatingFragmentPresenter(view, getLifecycle());
        List<Rating> ratings = Repository.getRatings(entityKey, new Callback<List<Rating>>() {
            @Override
            public void onSuccess(List<Rating> object) {
                presenter.present(object);
            }

            @Override
            public void onFailure() {

            }
        });
        presenter.present(ratings);

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
            if (Authentication.isLoggedIn(view.getContext())) {
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
