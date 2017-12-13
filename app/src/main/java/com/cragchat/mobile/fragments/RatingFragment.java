package com.cragchat.mobile.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.RateRouteActivity;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.view.adapters.recycler.RatingRecyclerAdapter;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;


public class RatingFragment extends Fragment implements View.OnClickListener {

    private String entityKey;
    private RatingRecyclerAdapter adapter;

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
        Repository.getRatings(entityKey, null);
        adapter = RatingRecyclerAdapter.create(entityKey, (CragChatActivity) getActivity());

        RecyclerView recyclerView = view.findViewById(R.id.list_ratings);
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
        this.getLifecycle().addObserver(adapter);

        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (Authentication.isLoggedIn(getContext())) {
                Intent intent = new Intent(getContext(), RateRouteActivity.class);
                intent.putExtra("entityKey", entityKey);
                getActivity().startActivity(intent);
            } else {
                //Log.d("RouteActivity", "Must be logged in to add an image");
                DialogFragment df = NotificationDialog.newInstance("Must be logged in to rate a route.");
                df.show(getFragmentManager(), "dialog");
            }
        }
    }
}
