package com.cragchat.mobile.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.database.models.RealmRating;
import com.cragchat.mobile.model.Rating;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.view.adapters.recycler.RatingRecyclerAdapter;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;

import java.util.List;

import io.realm.Realm;


public class ProfileRatingsFragment extends Fragment {

    private Realm mRealm;

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

        mRealm = Realm.getDefaultInstance();

        List<Rating> ratings = LocalDatabase.getInstance(getContext()).getProfileRatings(getActivity(), getArguments().getString("username"));

       /* final RatingRecyclerAdapter adapter = new RatingRecyclerAdapter(
                mRealm.where(RealmRating.class).equalTo(RealmRating.FIELD_ENTITY_KEY, )
        );
        RecyclerView recyclerView = view.findViewById(R.id.list_ratings);
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);*/

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

}
