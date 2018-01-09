package com.cragchat.mobile.ui.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.presenter.LocationFragmentPresenter;

public class LocationFragment extends Fragment implements View.OnClickListener {

    private CommentSectionFragment commentFragment;
    public static String ROUTE_KEY = "ROUTE_KEY";

    public static LocationFragment newInstance(String routeId) {
        LocationFragment f = new LocationFragment();
        Bundle b = new Bundle();
        b.putString(ROUTE_KEY, routeId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        String entityId = getArguments().getString(ROUTE_KEY);

        commentFragment = CommentSectionFragment.newInstance(entityId, CommentSectionFragment.TABLE_LOCATION);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_location, commentFragment);
        transaction.commit();

        LocationFragmentPresenter presenter = new LocationFragmentPresenter(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (commentFragment != null) {
            commentFragment.onClick(view);
        }
    }
}