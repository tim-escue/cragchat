package com.cragchat.mobile.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.RatingListAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_profile_ratings, container, false);

        List<Rating> ratings = LocalDatabase.getInstance(getContext()).getProfileRatings(getActivity(), getArguments().getString("username"));

        final RatingListAdapter adapter = new RatingListAdapter(getActivity(), ratings, true);
        ListView lv = (ListView) view.findViewById(R.id.list_ratings);

        if (ratings.size() == 0) {
            lv.setEmptyView(view.findViewById(R.id.list_empty));
        }

        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }


}
