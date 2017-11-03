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
import com.cragchat.mobile.database.models.RealmRating;
import com.cragchat.mobile.view.adapters.recycler.RatingRecyclerAdapter;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;
import com.cragchat.networkapi.ErrorHandlingObserverable;
import com.cragchat.networkapi.NetworkApi;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;


public class RatingFragment extends Fragment implements View.OnClickListener {

    private String entityKey;
    private Realm mRealm;

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

        NetworkApi.getInstance().getRatings(entityKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandlingObserverable<List<RealmRating>>() {
                    @Override
                    public void onSuccess(final List<RealmRating> object) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.insertOrUpdate(object);
                            }
                        });
                        realm.close();
                    }
                });

        mRealm = Realm.getDefaultInstance();

        RatingRecyclerAdapter adapter = new RatingRecyclerAdapter(
                mRealm.where(RealmRating.class).equalTo(RealmRating.FIELD_ENTITY_KEY, entityKey).findAll(),
                true,
                (CragChatActivity) getActivity()
        );
        RecyclerView recyclerView = view.findViewById(R.id.list_ratings);
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
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
