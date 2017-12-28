package com.cragchat.mobile.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.MapActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

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

    public class LocationFragmentPresenter {

        private Context context;

        @OnClick(R.id.show_map)
        void showMap() {
            Intent intent = new Intent(context, MapActivity.class);
            context.startActivity(intent);
        }

        public LocationFragmentPresenter(View view) {
            ButterKnife.bind(this, view);
            this.context = view.getContext();
        }
    }

    @Override
    public void onClick(View view) {
        if (commentFragment != null) {
            commentFragment.onClick(view);
        }
    }
}