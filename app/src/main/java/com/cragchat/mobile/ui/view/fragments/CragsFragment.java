package com.cragchat.mobile.ui.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.view.adapters.recycler.CragsFragmentRecyclerAdapter;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Created by timde on 9/9/2017.
 */

public class CragsFragment extends DaggerFragment {

    @Inject
    Repository repository;

    private static final String OZONE = "Ozone";

    @Inject
    public CragsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_crags, container, false);

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.crags_recycler);

        CragsFragmentRecyclerAdapter adapter = CragsFragmentRecyclerAdapter.create(OZONE, getActivity());
        getLifecycle().addObserver(adapter);

        RecyclerUtils.setAdapterAndManager(recList, adapter, LinearLayoutManager.VERTICAL);

        /*
            RecyclerView uses a RealmAdapter which depends on realm-specific api so Repository
            which uses abstract interfaces will not provide right type. We still call repository
            so that local database is updated from network. RealmAdapter will auto-update
            when the local database (Realm) is updated.
         */
        repository.getAreaByName("Ozone", null);


        return view;
    }


}
