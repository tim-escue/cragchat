package com.cragchat.mobile.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.RateRouteActivity;
import com.cragchat.mobile.adapters.RatingListAdapter;
import com.cragchat.mobile.descriptor.Rating;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.user.User;

import java.util.List;


public class RatingFragment extends Fragment implements View.OnClickListener {

    private int id;

    public static RatingFragment newInstance(int displayableId) {
        RatingFragment f = new RatingFragment();
        Bundle b = new Bundle();
        b.putString("id", "" + displayableId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_ratings, container, false);
        id = Integer.parseInt(getArguments().getString("id"));

        List<Rating> ratings = LocalDatabase.getInstance(getContext()).getRatingsFor(id);

        final RatingListAdapter adapter = new RatingListAdapter(getActivity(), ratings, false);
        ListView lv = (ListView) view.findViewById(R.id.list_ratings);

        if (ratings.size() == 0) {
            lv.setEmptyView(view.findViewById(R.id.list_empty));
        }

        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (User.currentToken(getActivity()) != null) {
                Intent next = new Intent(getContext(), RateRouteActivity.class);
                next.putExtra("id", id);
                startActivity(next);
            } else {
                Toast.makeText(getContext(), "Must be logged in to rate climb", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
