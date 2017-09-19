package com.cragchat.mobile.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.recycler.RatingRecyclerAdapter;
import com.cragchat.mobile.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.descriptor.Rating;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.List;


public class ProfileRatingsFragment extends Fragment {


    public static ProfileRatingsFragment newInstance(String username) {
        ProfileRatingsFragment f = new ProfileRatingsFragment();
        Bundle b = new Bundle();
        b.putString("username", username);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_profile_ratings, container, false);

        List<Rating> ratings = LocalDatabase.getInstance(getContext()).getProfileRatings(getActivity(), getArguments().getString("username"));

        final RatingRecyclerAdapter adapter = new RatingRecyclerAdapter(getActivity(), ratings, false);
        RecyclerView recyclerView = view.findViewById(R.id.list_ratings);
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);

        return view;
    }


}
