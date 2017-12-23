package com.cragchat.mobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cragchat.mobile.R;

public class LocationFragment extends Fragment implements View.OnClickListener {

    private CommentSectionFragment commentFragment;

    public static LocationFragment newInstance(String routeId) {
        LocationFragment f = new LocationFragment();
        Bundle b = new Bundle();
        b.putString("routeId", routeId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        String entityId = getArguments().getString("routeId");
        if (entityId == null) {
            Toast.makeText(getContext(), "NO ENTITYID _ LOCATION FRAG", Toast.LENGTH_LONG).show();
        }

        commentFragment = CommentSectionFragment.newInstance(entityId, "LOCATION");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_location, commentFragment);
        transaction.commit();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (commentFragment != null) {
            commentFragment.onClick(view);
        }
    }
}